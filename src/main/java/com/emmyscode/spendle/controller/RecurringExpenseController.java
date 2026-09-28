package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.RecurringExpenseRequestDTO;
import com.emmyscode.spendle.dtos.RecurringExpenseResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.service.RecurringExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for RecurringExpense records.
 * All operations are scoped to the authenticated user.
 */
@RestController
@RequestMapping("/api/recurring-expenses")
@RequiredArgsConstructor
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    @GetMapping
    public ResponseEntity<List<RecurringExpenseResponseDTO>> getAll(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(recurringExpenseService.getAllRecurringExpenses(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecurringExpenseResponseDTO> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(recurringExpenseService.getRecurringExpenseById(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<RecurringExpenseResponseDTO> create(
            @Valid @RequestBody RecurringExpenseRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recurringExpenseService.createRecurringExpense(dto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringExpenseResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody RecurringExpenseRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
                recurringExpenseService.updateRecurringExpense(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        recurringExpenseService.deleteRecurringExpense(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
