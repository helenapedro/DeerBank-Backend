package com.hmpedro.deerbank.dto.account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String accountNumber;

    @Min(1)
    private BigDecimal amount;
}