package com.emmyscode.spendle.dto;

import com.emmyscode.spendle.enums.Categories;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseRequestDTO(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Category is required")
        Categories category,

        @NotNull(message = "Date is required")
        LocalDateTime date,

        @NotNull(message = "User ID is required")
        UUID userId
) {
}
