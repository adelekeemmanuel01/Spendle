package com.emmyscode.spendle.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecurringExpenseRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Next payment date is required")
        LocalDateTime nextPaymentDate
) {
}
