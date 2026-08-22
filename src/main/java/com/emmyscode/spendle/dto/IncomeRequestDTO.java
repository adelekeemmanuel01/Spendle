package com.emmyscode.spendle.dto;

import com.emmyscode.spendle.enums.IncomeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record IncomeRequestDTO(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Source is required")
        String source,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Income type is required")
        IncomeType type,

        @NotNull(message = "User ID is required")
        UUID userId
) {
}
