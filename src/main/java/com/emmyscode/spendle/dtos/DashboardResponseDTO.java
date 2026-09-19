package com.emmyscode.spendle.dtos;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        BigDecimal totalIncome,
        BigDecimal totalRecurringExpenses,
        BigDecimal totalBudget,
        BigDecimal totalSavingsAllocation,
        BigDecimal safeToSpend
) {
}
