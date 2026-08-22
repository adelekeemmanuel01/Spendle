package com.emmyscode.spendle.dto;

import com.emmyscode.spendle.enums.IncomeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record IncomeResponseDTO(
        UUID id,
        BigDecimal amount,
        String source,
        LocalDate date,
        IncomeType type,
        UUID userId
) {
}
