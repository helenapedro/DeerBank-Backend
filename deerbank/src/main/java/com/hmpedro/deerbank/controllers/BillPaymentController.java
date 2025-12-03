package com.hmpedro.deerbank.controllers;

import com.hmpedro.deerbank.dto.billpay.*;
import com.hmpedro.deerbank.services.BillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bill-payments")
@RequiredArgsConstructor
public class BillPaymentController {

    private final BillPaymentService billPaymentService;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getDetails() == null) {
            throw new IllegalStateException("No authenticated user in context");
        }
        return (Long) auth.getDetails();
    }

    @PostMapping("/pay")
    public ResponseEntity<PayBillResponse> payBill(@RequestBody PayBillRequest request) {
        Long userId = getCurrentUserId();
        PayBillResponse response = billPaymentService.payBill(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recurring")
    public ResponseEntity<ScheduleRecurringPaymentResponse> scheduleRecurring(
            @RequestBody ScheduleRecurringPaymentRequest request
    ) {
        Long userId = getCurrentUserId();
        ScheduleRecurringPaymentResponse response =
                billPaymentService.scheduleRecurringPayment(userId, request);
        return ResponseEntity.ok(response);
    }

    // For demo/manual trigger of recurring processing
    @PostMapping("/process-recurring")
    public ResponseEntity<ProcessRecurringPaymentsResponse> processRecurring() {
        int count = billPaymentService.processRecurringPayments();
        return ResponseEntity.ok(
                new ProcessRecurringPaymentsResponse("Recurring payments processed", count)
        );
    }
}
