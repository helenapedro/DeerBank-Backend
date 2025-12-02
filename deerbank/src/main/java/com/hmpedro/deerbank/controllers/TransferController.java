package com.hmpedro.deerbank.controllers;

import com.hmpedro.deerbank.dto.transfer.*;
import com.hmpedro.deerbank.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/own")
    public ResponseEntity<TransferResponse> transferBetweenOwnAccounts(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody TransferBetweenOwnAccountsRequest request
    ) {
        TransferResponse response = transferService.transferBetweenOwnAccounts(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/other")
    public ResponseEntity<TransferResponse> transferToOtherCustomer(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody TransferToOtherCustomerRequest request
    ) {
        TransferResponse response = transferService.transferToOtherCustomer(userId, request);
        return ResponseEntity.ok(response);
    }
}
