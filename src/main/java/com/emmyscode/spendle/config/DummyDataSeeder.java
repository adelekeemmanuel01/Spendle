package com.emmyscode.spendle.config;

import com.emmyscode.spendle.enums.Categories;
import com.emmyscode.spendle.enums.IncomeType;
import com.emmyscode.spendle.enums.Role;
import com.emmyscode.spendle.model.*;
import com.emmyscode.spendle.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DummyDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Database is empty. Loading dummy data...");
            loadUsersAndData();
            log.info("Dummy data loading complete.");
        } else {
            log.info("Database already contains data. Skipping dummy data seeder.");
        }
    }

    private void loadUsersAndData() {
        // User 1
        User user1 = new User();
        user1.setFullName("Alice Smith");
        user1.setEmail("alice@example.com");
        user1.setPhoneNumber("08012345678");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRegisteredDate(LocalDate.now().minusDays(10));
        user1.setRole(Role.USER);
        user1 = userRepository.save(user1);

        // User 1 Data
        createIncome(user1, "500000.00", IncomeType.SALARY);
        createIncome(user1, "150000.00", IncomeType.FREELANCE);

        createBudget(user1, "80000.00", Categories.FOOD);
        createBudget(user1, "50000.00", Categories.TRANSPORT);

        createSavingsGoal(user1, "1000000.00", "50000.00", "200000.00", LocalDate.now().plusMonths(6));
        
        createRecurringExpense(user1, "Netflix Subscription", "5000.00", LocalDateTime.now().plusDays(5));
        createRecurringExpense(user1, "Gym Membership", "15000.00", LocalDateTime.now().plusDays(15));


        // User 2
        User user2 = new User();
        user2.setFullName("Bob Johnson");
        user2.setEmail("bob@example.com");
        user2.setPhoneNumber("09098765432");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setRegisteredDate(LocalDate.now().minusDays(5));
        user2.setRole(Role.USER);
        user2 = userRepository.save(user2);

        // User 2 Data
        createIncome(user2, "750000.00", IncomeType.BUSINESS);
        
        createBudget(user2, "120000.00", Categories.FAMILY);
        createBudget(user2, "60000.00", Categories.RENT);

        createSavingsGoal(user2, "5000000.00", "200000.00", "400000.00", LocalDate.now().plusYears(1));
        
        createRecurringExpense(user2, "Internet Bill", "25000.00", LocalDateTime.now().plusDays(2));
    }

    private void createIncome(User user, String amount, IncomeType type) {
        Income income = new Income();
        income.setUser(user);
        income.setAmount(new BigDecimal(amount));
        income.setType(type);
        incomeRepository.save(income);
    }

    private void createBudget(User user, String amount, Categories category) {
        Budget budget = new Budget();
        budget.setUser(user);
        budget.setAmount(new BigDecimal(amount));
        budget.setCategory(category);
        budgetRepository.save(budget);
    }

    private void createSavingsGoal(User user, String targetAmount, String monthlyAllocation, String currentAmount, LocalDate targetDate) {
        SavingsGoal goal = new SavingsGoal();
        goal.setUser(user);
        goal.setTargetAmount(new BigDecimal(targetAmount));
        goal.setMonthlyAllocation(new BigDecimal(monthlyAllocation));
        goal.setCurrentAmount(new BigDecimal(currentAmount));
        goal.setTargetDate(targetDate);
        savingsGoalRepository.save(goal);
    }

    private void createRecurringExpense(User user, String name, String amount, LocalDateTime nextPaymentDate) {
        RecurringExpense expense = new RecurringExpense();
        expense.setUser(user);
        expense.setName(name);
        expense.setAmount(new BigDecimal(amount));
        expense.setNextPaymentDate(nextPaymentDate);
        recurringExpenseRepository.save(expense);
    }
}
