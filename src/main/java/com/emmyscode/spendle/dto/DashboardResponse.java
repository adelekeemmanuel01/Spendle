package com.emmyscode.spendle.dto;

import java.math.BigDecimal;

public record DashboardResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal totalCommitments,
        BigDecimal totalSavings,
        BigDecimal safeToSpend
) {
}
