package com.emmyscode.spendle.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingsGoalRequestDTO(
        @NotNull(message = "Target amount is required")
        @Positive(message = "Target amount must be greater than zero")
        BigDecimal targetAmount,

        @NotNull(message = "Monthly allocation is required")
        @Positive(message = "Monthly allocation must be greater than zero")
        BigDecimal monthlyAllocation,

        @PositiveOrZero(message = "Current amount cannot be negative")
        BigDecimal currentAmount,

        @NotNull(message = "Target date is required")
        LocalDate targetDate
) {
}
