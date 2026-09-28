package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.DashboardResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.BudgetRepository;
import com.emmyscode.spendle.repositories.IncomeRepository;
import com.emmyscode.spendle.repositories.RecurringExpenseRepository;
import com.emmyscode.spendle.repositories.SavingsGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public DashboardResponseDTO getDashboard(User currentUser) {
        UUID userId = currentUser.getId();

        BigDecimal totalIncome              = incomeRepository.sumAmountByUserId(userId);
        BigDecimal totalRecurringExpenses   = recurringExpenseRepository.sumAmountByUserId(userId);
        BigDecimal totalBudget              = budgetRepository.sumAmountByUserId(userId);
        BigDecimal totalSavingsAllocation   = savingsGoalRepository.sumMonthlyAllocationByUserId(userId);

        // safeToSpend = income − recurring expenses − savings allocation
        BigDecimal safeToSpend = totalIncome
                .subtract(totalRecurringExpenses)
                .subtract(totalSavingsAllocation);

        return new DashboardResponseDTO(
                totalIncome,
                totalRecurringExpenses,
                totalBudget,
                totalSavingsAllocation,
                safeToSpend
        );
    }
}
