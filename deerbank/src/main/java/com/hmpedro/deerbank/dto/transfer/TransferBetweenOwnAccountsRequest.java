package com.hmpedro.deerbank.dto.transfer;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferBetweenOwnAccountsRequest {
    private String sourceAccountNumber, targetAccountNumber;
    private BigDecimal amount;
}
