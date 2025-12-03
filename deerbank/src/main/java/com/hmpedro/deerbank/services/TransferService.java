package com.hmpedro.deerbank.services;

import com.hmpedro.deerbank.dto.transfer.*;
import com.hmpedro.deerbank.entities.Account;
import com.hmpedro.deerbank.entities.Transaction;
import com.hmpedro.deerbank.entities.TransactionType;
import com.hmpedro.deerbank.repositories.AccountRepository;
import com.hmpedro.deerbank.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransferResponse transferBetweenOwnAccounts(
            Long userId,
            TransferBetweenOwnAccountsRequest request
    ) {
        validateOwnTransferRequest(request);

        if (request.getSourceAccountNumber().equals(request.getTargetAccountNumber())) {
            throw new IllegalArgumentException("Source and target accounts must be different");
        }

        Account source = findAccountOwnedByUser(request.getSourceAccountNumber(), userId);
        Account target = findAccountOwnedByUser(request.getTargetAccountNumber(), userId);

        return executeTransfer(source, target, request.getAmount(), true);
    }

    @Transactional
    public TransferResponse transferToOtherCustomer(
            Long userId,
            TransferToOtherCustomerRequest request
    ) {
        validateOtherTransferRequest(request);

        if (request.getSourceAccountNumber().equals(request.getTargetAccountNumber())) {
            throw new IllegalArgumentException("Source and target accounts must be different");
        }

        Account source = findAccountOwnedByUser(request.getSourceAccountNumber(), userId);
        Account target = accountRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        return executeTransfer(source, target, request.getAmount(), false);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private void validateOwnTransferRequest(TransferBetweenOwnAccountsRequest request) {
        if (request.getSourceAccountNumber() == null || request.getTargetAccountNumber() == null) {
            throw new IllegalArgumentException("sourceAccountNumber and targetAccountNumber are required");
        }
        validateAmount(request.getAmount());
    }

    private void validateOtherTransferRequest(TransferToOtherCustomerRequest request) {
        if (request.getSourceAccountNumber() == null || request.getTargetAccountNumber() == null) {
            throw new IllegalArgumentException("sourceAccountNumber and targetAccountNumber are required");
        }
        validateAmount(request.getAmount());
    }

    private Account findAccountOwnedByUser(String accountNumber, Long userId) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        if (acc.getOwner() == null || !acc.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to the logged-in user");
        }
        return acc;
    }

    private TransferResponse executeTransfer(
            Account source,
            Account target,
            BigDecimal amount,
            boolean ownTransfer
    ) {
        if (source.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds in source account");
        }

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(target);

        Transaction debitTx = Transaction.builder()
                .account(source)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .description(ownTransfer
                        ? "Transfer to own account " + target.getAccountNumber()
                        : "Transfer to account " + target.getAccountNumber())
                .build();

        Transaction creditTx = Transaction.builder()
                .account(target)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .description(ownTransfer
                        ? "Transfer from own account " + source.getAccountNumber()
                        : "Transfer from account " + source.getAccountNumber())
                .build();

        transactionRepository.save(debitTx);
        transactionRepository.save(creditTx);

        return TransferResponse.builder()
                .message("Transfer executed successfully")
                .sourceAccountNumber(source.getAccountNumber())
                .targetAccountNumber(target.getAccountNumber())
                .sourceNewBalance(source.getBalance())
                .targetNewBalance(target.getBalance())
                .build();
    }
}
