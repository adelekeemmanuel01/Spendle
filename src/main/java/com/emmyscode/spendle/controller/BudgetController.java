package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.BudgetRequestDTO;
import com.emmyscode.spendle.dtos.BudgetResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for Budget records.
 * All operations are scoped to the authenticated user.
 */
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> getAll(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.getAllBudgets(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.getBudgetById(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> create(
            @Valid @RequestBody BudgetRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(budgetService.createBudget(dto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody BudgetRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.updateBudget(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        budgetService.deleteBudget(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
