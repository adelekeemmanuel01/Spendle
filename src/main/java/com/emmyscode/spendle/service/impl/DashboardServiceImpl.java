package com.emmyscode.spendle.service.impl;

import com.emmyscode.spendle.dto.DashboardResponse;
import com.emmyscode.spendle.repositories.BudgetRepository;
import com.emmyscode.spendle.repositories.ExpenseRepository;
import com.emmyscode.spendle.repositories.IncomeRepository;
import com.emmyscode.spendle.repositories.SavingsGoalRepository;
import com.emmyscode.spendle.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository savingsGoalRepository;


    @Override
    public DashboardResponse getDashboard(UUID userId) {

        BigDecimal totalIncome =
                incomeRepository.getTotalIncome(userId);

        BigDecimal totalExpenses =
                expenseRepository.getTotalExpenses(userId);

        BigDecimal totalCommitments = budgetRepository.getTotalBudget(userId);

        BigDecimal totalSavings = savingsGoalRepository.getTotalSavingsAllocation(userId);

        BigDecimal safeToSpend =
                totalIncome
                        .subtract(totalExpenses)
                        .subtract(totalCommitments)
                        .subtract(totalSavings);

        return new DashboardResponse(
                totalIncome,
                totalExpenses,
                totalCommitments,
                totalSavings,
                safeToSpend
        );
    }

}
