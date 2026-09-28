package com.emmyscode.spendle.repositories;

import com.emmyscode.spendle.model.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, UUID> {

    List<RecurringExpense> findByUserId(UUID userId);

    Optional<RecurringExpense> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RecurringExpense r WHERE r.user.id = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") UUID userId);
}
