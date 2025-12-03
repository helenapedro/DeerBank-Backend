package com.hmpedro.deerbank.services;

import com.hmpedro.deerbank.dto.billpay.*;
import com.hmpedro.deerbank.entities.*;
import com.hmpedro.deerbank.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BillPaymentService {

    private final AccountRepository accountRepository;
    private final PayeeRepository payeeRepository;
    private final BillPaymentRepository billPaymentRepository;
    private final RecurringPaymentRepository recurringPaymentRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public PayBillResponse payBill(Long userId, PayBillRequest request) {
        validateAmount(request.getAmount());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getAccountNumber()));

        if (account.getOwner() == null || !account.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to logged-in user");
        }

        Payee payee = payeeRepository.findById(request.getPayeeId())
                .orElseThrow(() -> new IllegalArgumentException("Payee not found: " + request.getPayeeId()));

        if (payee.getOwner() == null || !payee.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Payee does not belong to logged-in user");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledDate = request.getScheduledDate();
        boolean executeNow = (scheduledDate == null || !scheduledDate.isAfter(now));

        BillPayment bill = new BillPayment();
        bill.setUser(user);
        bill.setAccount(account);
        bill.setPayee(payee);
        bill.setAmount(request.getAmount());
        bill.setPaymentType(PaymentType.ONE_TIME);
        bill.setScheduledDate(scheduledDate != null ? scheduledDate : now);

        if (executeNow) {
            // execute immediately
            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new IllegalStateException("Insufficient funds for bill payment");
            }

            account.setBalance(account.getBalance().subtract(request.getAmount()));
            accountRepository.save(account);

            bill.setStatus(BillPaymentStatus.EXECUTED);
            bill.setExecutedDate(now);
            bill = billPaymentRepository.save(bill);

            // create transaction
            Transaction tx = Transaction.builder()
                    .account(account)
                    .type(TransactionType.BILL_PAYMENT)
                    .amount(request.getAmount())
                    .description("Bill payment to " + payee.getName())
                    .build();
            transactionRepository.save(tx);

            return PayBillResponse.builder()
                    .message("Bill payment processed")
                    .billPaymentId(bill.getId())
                    .accountNumber(account.getAccountNumber())
                    .payeeName(payee.getName())
                    .amount(bill.getAmount())
                    .status(bill.getStatus().name())
                    .newBalance(account.getBalance())
                    .build();
        } else {
            // schedule for future
            bill.setStatus(BillPaymentStatus.SCHEDULED);
            bill = billPaymentRepository.save(bill);

            return PayBillResponse.builder()
                    .message("Bill payment scheduled")
                    .billPaymentId(bill.getId())
                    .accountNumber(account.getAccountNumber())
                    .payeeName(payee.getName())
                    .amount(bill.getAmount())
                    .status(bill.getStatus().name())
                    .newBalance(account.getBalance())
                    .build();
        }
    }

    @Transactional
    public ScheduleRecurringPaymentResponse scheduleRecurringPayment(
            Long userId,
            ScheduleRecurringPaymentRequest request
    ) {
        validateAmount(request.getAmount());

        if (request.getStartDate() == null) {
            throw new IllegalArgumentException("startDate is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getAccountNumber()));

        if (account.getOwner() == null || !account.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to logged-in user");
        }

        Payee payee = payeeRepository.findById(request.getPayeeId())
                .orElseThrow(() -> new IllegalArgumentException("Payee not found: " + request.getPayeeId()));

        if (payee.getOwner() == null || !payee.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Payee does not belong to logged-in user");
        }

        RecurringPayment rp = new RecurringPayment();
        rp.setUser(user);
        rp.setAccount(account);
        rp.setPayee(payee);
        rp.setAmount(request.getAmount());
        rp.setFrequency(request.getFrequency());
        rp.setStartDate(request.getStartDate());
        rp.setEndDate(request.getEndDate());
        rp.setStatus(RecurringPaymentStatus.ACTIVE);

        rp.setNextExecutionDate(request.getStartDate().atStartOfDay());

        rp = recurringPaymentRepository.save(rp);

        return ScheduleRecurringPaymentResponse.builder()
                .message("Recurring payment scheduled")
                .recurringPaymentId(rp.getId())
                .accountNumber(account.getAccountNumber())
                .payeeName(payee.getName())
                .amount(rp.getAmount())
                .frequency(rp.getFrequency())
                .startDate(rp.getStartDate())
                .endDate(rp.getEndDate())
                .status(rp.getStatus().name())
                .build();
    }

    @Transactional
    public int processRecurringPayments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        var activeList = recurringPaymentRepository.findByStatus(RecurringPaymentStatus.ACTIVE);
        int processedCount = 0;

        for (RecurringPayment rp : activeList) {
            if (rp.getNextExecutionDate() == null) {
                continue;
            }
            if (rp.getNextExecutionDate().isAfter(now)) {
                continue; // not yet due
            }
            if (rp.getEndDate() != null && rp.getEndDate().isBefore(today)) {
                // end date already passed, mark as CANCELLED
                rp.setStatus(RecurringPaymentStatus.CANCELLED);
                recurringPaymentRepository.save(rp);
                continue;
            }

            Account account = rp.getAccount();
            Payee payee = rp.getPayee();
            User user = rp.getUser();

            if (account.getBalance().compareTo(rp.getAmount()) < 0) {
                // Not enough funds: for now we skip, but we could log or change status
                // We still advance nextExecutionDate to avoid infinite loop
                rp.setNextExecutionDate(calculateNextExecutionDate(rp));
                recurringPaymentRepository.save(rp);
                continue;
            }

            account.setBalance(account.getBalance().subtract(rp.getAmount()));
            accountRepository.save(account);

            BillPayment bill = new BillPayment();
            bill.setUser(user);
            bill.setAccount(account);
            bill.setPayee(payee);
            bill.setRecurringPayment(rp);
            bill.setPaymentType(PaymentType.RECURRING);
            bill.setAmount(rp.getAmount());
            bill.setScheduledDate(rp.getNextExecutionDate());
            bill.setStatus(BillPaymentStatus.EXECUTED);
            bill.setExecutedDate(now);
            bill = billPaymentRepository.save(bill);

            Transaction tx = Transaction.builder()
                    .account(account)
                    .type(TransactionType.BILL_PAYMENT)
                    .amount(rp.getAmount())
                    .description("Recurring payment to " + payee.getName())
                    .build();
            transactionRepository.save(tx);

            processedCount++;

            LocalDateTime next = calculateNextExecutionDate(rp);
            rp.setNextExecutionDate(next);

            if (rp.getEndDate() != null && next.toLocalDate().isAfter(rp.getEndDate())) {
                rp.setStatus(RecurringPaymentStatus.CANCELLED);
            }

            recurringPaymentRepository.save(rp);
        }
        return processedCount;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private LocalDateTime calculateNextExecutionDate(RecurringPayment rp) {
        LocalDateTime base = rp.getNextExecutionDate() != null
                ? rp.getNextExecutionDate()
                : rp.getStartDate().atStartOfDay();

        if (rp.getFrequency() == null) {
            return base.plusDays(1); // default
        }

        String freq = rp.getFrequency().toUpperCase();
        return switch (freq) {
            case "WEEKLY" -> base.plusWeeks(1);
            case "MONTHLY" -> base.plusMonths(1);
            default -> base.plusDays(1); // fallback
        };
    }
}
