package com.hmpedro.deerbank.dto.account;

import com.hmpedro.deerbank.entities.AccountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    @NotNull
    private Long userId;

    @NotNull
    private AccountType accountType;

    @Min(0)
    private BigDecimal initialBalance = BigDecimal.ZERO;
}
