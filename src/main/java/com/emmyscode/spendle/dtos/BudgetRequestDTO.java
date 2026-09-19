package com.emmyscode.spendle.dtos;

import com.emmyscode.spendle.enums.Categories;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BudgetRequestDTO(
        @NotNull(message = "Category is required")
        Categories category,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
