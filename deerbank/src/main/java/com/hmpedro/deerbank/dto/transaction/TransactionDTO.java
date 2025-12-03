package com.hmpedro.deerbank.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private String accountNumber;
    private String type;  // DEBIT, CREDIT, BILL_PAYMENT, etc.
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
}
