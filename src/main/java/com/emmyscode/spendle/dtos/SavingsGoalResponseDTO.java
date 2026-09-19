package com.emmyscode.spendle.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SavingsGoalResponseDTO(
        UUID id,
        BigDecimal targetAmount,
        BigDecimal monthlyAllocation,
        BigDecimal currentAmount,
        LocalDate targetDate
) {
}
