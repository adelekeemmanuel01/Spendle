package com.emmyscode.spendle.model;

import com.emmyscode.spendle.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    // ── UserDetails interface ─────────────────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /** Spring Security uses this for authentication; we authenticate by email. */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired()  { return true; }
    @Override
    public boolean isAccountNonLocked()   { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled()            { return true; }

    // ── Associations ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "user")
    private List<RecurringExpense> recurringExpenses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Income> incomes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Budget> budgets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SavingsGoal> savingsGoals = new ArrayList<>();
}

