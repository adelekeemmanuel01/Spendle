package com.emmyscode.spendle.repositories;

import com.emmyscode.spendle.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    List<Budget> findByUserId(UUID userId);

    Optional<Budget> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Budget b WHERE b.user.id = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") UUID userId);
}
