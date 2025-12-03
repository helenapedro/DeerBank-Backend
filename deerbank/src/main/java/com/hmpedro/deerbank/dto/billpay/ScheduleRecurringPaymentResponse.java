// ScheduleRecurringPaymentResponse.java
package com.hmpedro.deerbank.dto.billpay;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScheduleRecurringPaymentResponse {
    private String message;
    private Long recurringPaymentId;
    private String accountNumber;
    private String payeeName;
    private BigDecimal amount;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
