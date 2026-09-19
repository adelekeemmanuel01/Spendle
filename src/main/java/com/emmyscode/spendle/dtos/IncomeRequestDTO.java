package com.emmyscode.spendle.dtos;

import com.emmyscode.spendle.enums.IncomeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IncomeRequestDTO(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Income type is required")
        IncomeType type
) {
}
