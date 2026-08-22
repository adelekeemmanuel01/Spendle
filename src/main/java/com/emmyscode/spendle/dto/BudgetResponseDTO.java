package com.emmyscode.spendle.dto;

import com.emmyscode.spendle.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BudgetResponseDTO(
        UUID id,
        Categories category,
        BigDecimal amount,
        LocalDate startDate,
        LocalDate endDate,
        UUID userId
) {
}
