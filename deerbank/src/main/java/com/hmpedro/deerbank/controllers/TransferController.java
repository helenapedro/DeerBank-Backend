package com.hmpedro.deerbank.controllers;

import com.hmpedro.deerbank.dto.transfer.*;
import com.hmpedro.deerbank.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/own")
    public ResponseEntity<TransferResponse> transferBetweenOwnAccounts(
            @RequestBody TransferBetweenOwnAccountsRequest request
    ) {
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        TransferResponse response = transferService.transferBetweenOwnAccounts(currentUserId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/other")
    public ResponseEntity<TransferResponse> transferToOtherCustomer(
            @RequestBody TransferToOtherCustomerRequest request
    ) {
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        TransferResponse response = transferService.transferToOtherCustomer(currentUserId, request);
        return ResponseEntity.ok(response);
    }
}
