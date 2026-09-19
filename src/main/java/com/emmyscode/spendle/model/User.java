package com.emmyscode.spendle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private LocalDate registeredDate;

    @OneToMany(mappedBy = "user")
    private List<RecurringExpense> recurringExpenses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Income> incomes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Budget> budgets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SavingsGoal> savingsGoals = new ArrayList<>();

}
