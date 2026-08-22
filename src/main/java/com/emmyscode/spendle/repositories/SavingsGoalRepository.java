package com.emmyscode.spendle.repositories;

import com.emmyscode.spendle.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, UUID> {

    List<SavingsGoal> findByUserId(UUID userId);

    Optional<SavingsGoal> findByIdAndUserId(UUID id, UUID userId);

    @Query("""
            SELECT COALESCE(SUM(s.monthlyAllocation), 0)
            FROM SavingsGoal s
            WHERE s.user.id = :userId
            """)
    BigDecimal getTotalSavingsAllocation(@Param("userId") UUID userId);

}
