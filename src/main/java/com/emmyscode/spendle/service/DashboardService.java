package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.DashboardResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.BudgetRepository;
import com.emmyscode.spendle.repositories.IncomeRepository;
import com.emmyscode.spendle.repositories.RecurringExpenseRepository;
import com.emmyscode.spendle.repositories.SavingsGoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboard(User currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            log.warn("getDashboard called with null user principal");
            return new DashboardResponseDTO(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        UUID userId = currentUser.getId();

        BigDecimal rawIncome = incomeRepository.sumAmountByUserId(userId);
        BigDecimal rawRecurring = recurringExpenseRepository.sumAmountByUserId(userId);
        BigDecimal rawBudget = budgetRepository.sumAmountByUserId(userId);
        BigDecimal rawSavings = savingsGoalRepository.sumMonthlyAllocationByUserId(userId);

        BigDecimal totalIncome = rawIncome != null ? rawIncome : BigDecimal.ZERO;
        BigDecimal totalRecurringExpenses = rawRecurring != null ? rawRecurring : BigDecimal.ZERO;
        BigDecimal totalBudget = rawBudget != null ? rawBudget : BigDecimal.ZERO;
        BigDecimal totalSavingsAllocation = rawSavings != null ? rawSavings : BigDecimal.ZERO;

        // safeToSpend = income − recurring expenses - budget − savings allocation
        BigDecimal safeToSpend = totalIncome
                .subtract(totalRecurringExpenses)
                .subtract(totalBudget)
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
