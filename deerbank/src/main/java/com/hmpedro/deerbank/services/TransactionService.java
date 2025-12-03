package com.hmpedro.deerbank.services;

import com.hmpedro.deerbank.dto.transaction.TransactionDTO;
import com.hmpedro.deerbank.entities.Transaction;
import com.hmpedro.deerbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    /**
     * Returns all transactions of all accounts owned by the given user,
     * ordered from newest to oldest.
     */
    public List<TransactionDTO> getTransactionsForUser(Long userId) {
        List<Transaction> txList = transactionRepository
                .findByUserIdOrderByCreatedAtDesc(userId);

        return txList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO toDTO(Transaction tx) {
        return TransactionDTO.builder()
                .id(tx.getId())
                .accountNumber(tx.getAccount().getAccountNumber())
                .type(tx.getType().name())
                .amount(tx.getAmount())
                .description(tx.getDescription())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
