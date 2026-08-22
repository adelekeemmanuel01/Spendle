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

    private String name;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}