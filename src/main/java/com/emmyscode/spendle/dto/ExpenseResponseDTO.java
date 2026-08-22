package com.emmyscode.spendle.dto;

import com.emmyscode.spendle.enums.Categories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponseDTO(
        UUID id,
        BigDecimal amount,
        String description,
        Categories category,
        LocalDateTime date,
        UUID userId
) {
}
