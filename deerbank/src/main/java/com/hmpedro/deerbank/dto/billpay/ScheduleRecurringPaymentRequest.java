package com.hmpedro.deerbank.dto.billpay;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScheduleRecurringPaymentRequest {
    private String accountNumber;
    private Long payeeId;
    private BigDecimal amount;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
}
