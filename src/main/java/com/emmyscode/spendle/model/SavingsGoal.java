package com.emmyscode.spendle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "saving_goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoal {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

//  The total they're aiming to reach
    private BigDecimal targetAmount;

//  How much they want to save per month
    private BigDecimal monthlyAllocation;

//  How much they've saved so far toward it
    private BigDecimal currentAmount;

    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}