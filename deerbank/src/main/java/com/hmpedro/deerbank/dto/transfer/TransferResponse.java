package com.hmpedro.deerbank.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {
    private String message,
            sourceAccountNumber,
            targetAccountNumber;
    private BigDecimal sourceNewBalance, targetNewBalance;
}
