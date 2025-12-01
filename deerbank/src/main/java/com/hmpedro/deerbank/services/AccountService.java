package com.hmpedro.deerbank.services;

import com.hmpedro.deerbank.dto.account.AccountResponse;
import com.hmpedro.deerbank.dto.account.CreateAccountRequest;
import com.hmpedro.deerbank.dto.account.DepositRequest;
import com.hmpedro.deerbank.dto.account.WithdrawRequest;
import com.hmpedro.deerbank.entities.*;
import com.hmpedro.deerbank.repositories.AccountRepository;
import com.hmpedro.deerbank.repositories.TransactionRepository;
import com.hmpedro.deerbank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User owner = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // generate simple acc num for demo
        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .owner(owner)
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .balance(
                        request.getInitialBalance() != null
                                ? request.getInitialBalance()
                                : BigDecimal.ZERO
                )
                .build();

        Account saved = accountRepository.save(account);

        // create a credit transaction if amount > 0
        if (saved.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            Transaction tx = Transaction.builder()
                    .account(saved)
                    .type(TransactionType.CREDIT)
                    .amount(saved.getBalance())
                    .description("Initial deposit")
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionRepository.save(tx);
        }
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUser(Long userId) {
        List<Account> accounts = accountRepository.findByOwnerId(userId);
        return accounts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse deposit(DepositRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }

        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);

        Transaction tx = Transaction.builder()
                .account(account)
                .type(TransactionType.CREDIT)
                .amount(request.getAmount())
                .description("Deposit")
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);
        Account saved = accountRepository.save(account);

        return toResponse(saved);
    }

    @Transactional
    public AccountResponse withdraw(WithdrawRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getOwner().getId().equals(request.getUserId())) {
            throw new IllegalArgumentException("This account does not belong to the user");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);

        Transaction tx = Transaction.builder()
                .account(account)
                .type(TransactionType.DEBIT)
                .amount(request.getAmount())
                .description("Withdrawal")
                .build();

        transactionRepository.save(tx);
        Account saved = accountRepository.save(account);

        return toResponse(saved);
    }

    // ========= HELPERS =========
    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }

    private String generateAccountNumber() {
        Random random = new Random();
        long number = Math.abs(random.nextLong()) % 1_000_000_0000L; // 10 digits
        return String.format("ACC%010d", number);
    }
}
