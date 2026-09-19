package com.emmyscode.spendle.dtos;

import com.emmyscode.spendle.enums.IncomeType;

import java.math.BigDecimal;
import java.util.UUID;

public record IncomeResponseDTO(
        UUID id,
        BigDecimal amount,
        IncomeType type
) {
}
