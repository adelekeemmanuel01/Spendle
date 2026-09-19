package com.emmyscode.spendle.dtos;

import com.emmyscode.spendle.enums.Categories;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetResponseDTO(
        UUID id,
        Categories category,
        BigDecimal amount
) {
}
