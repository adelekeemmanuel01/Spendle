package com.emmyscode.spendle.model;

import com.emmyscode.spendle.enums.IncomeType;
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
@Table(name = "income")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Income {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private IncomeType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
