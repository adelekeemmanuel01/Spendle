package com.emmyscode.spendle.repositories;

import com.emmyscode.spendle.enums.Categories;
import com.emmyscode.spendle.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    List<Expense> findByUserId(UUID userId);

    List<Expense> findByUserIdAndCategory(UUID userId, Categories category);

    List<Expense> findByUserIdAndDateBetween(UUID userId, LocalDateTime start, LocalDateTime end);

    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user.id = :userId
            """)
    BigDecimal getTotalExpenses(@Param("userId") UUID userId);
}
