package com.hmpedro.deerbank.dto.billpay;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PayBillResponse {
    private String message;
    private Long billPaymentId;
    private String accountNumber;
    private String payeeName;
    private BigDecimal amount;
    private String status;
    private BigDecimal newBalance;
}
