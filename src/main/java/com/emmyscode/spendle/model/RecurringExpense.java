package com.emmyscode.spendle.model;

import com.emmyscode.spendle.dto.RecurringExpenseRequestDTO;
import com.emmyscode.spendle.enums.Categories;
import com.emmyscode.spendle.enums.RecurringFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recurring_expense")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecurringExpense {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private BigDecimal amount;

    private LocalDateTime nextPaymentDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
