package com.hmpedro.deerbank.dto.billpay;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayBillRequest {
    private String accountNumber;
    private Long payeeId;
    private BigDecimal amount;
    private LocalDateTime scheduledDate;
}
