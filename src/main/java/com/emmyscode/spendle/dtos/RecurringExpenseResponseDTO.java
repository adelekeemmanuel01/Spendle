package com.emmyscode.spendle.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RecurringExpenseResponseDTO(
        UUID id,
        String name,
        BigDecimal amount,
        LocalDateTime nextPaymentDate
) {
}
