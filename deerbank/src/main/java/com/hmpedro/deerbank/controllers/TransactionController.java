package com.hmpedro.deerbank.controllers;

import com.hmpedro.deerbank.dto.transaction.TransactionDTO;
import com.hmpedro.deerbank.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    /**
     * Returns all transactions for all accounts of the logged-in user.
     */
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getMyTransactions() {
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();
        List<TransactionDTO> transactions =
                transactionService.getTransactionsForUser(currentUserId);
        return ResponseEntity.ok(transactions);
    }
}
