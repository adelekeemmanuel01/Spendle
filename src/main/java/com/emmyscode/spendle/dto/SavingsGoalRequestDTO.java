package com.emmyscode.spendle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SavingsGoalRequestDTO(
        @NotBlank(message = "Goal name is required")
        String name,

        @NotNull(message = "Target amount is required")
        @Positive(message = "Target amount must be positive")
        BigDecimal targetAmount,

        @NotNull(message = "Current amount is required")
        @PositiveOrZero(message = "Current amount cannot be negative")
        BigDecimal currentAmount,

        @NotNull(message = "Target date is required")
        LocalDate targetDate,

        @NotNull(message = "User ID is required")
        UUID userId
) {
}
