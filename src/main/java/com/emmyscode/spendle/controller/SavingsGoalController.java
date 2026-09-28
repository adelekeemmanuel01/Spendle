package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.SavingsGoalRequestDTO;
import com.emmyscode.spendle.dtos.SavingsGoalResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.service.SavingsGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for SavingsGoal records.
 * All operations are scoped to the authenticated user.
 */
@RestController
@RequestMapping("/api/savings-goals")
@RequiredArgsConstructor
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    @GetMapping
    public ResponseEntity<List<SavingsGoalResponseDTO>> getAll(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(savingsGoalService.getAllSavingsGoals(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponseDTO> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(savingsGoalService.getSavingsGoalById(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<SavingsGoalResponseDTO> create(
            @Valid @RequestBody SavingsGoalRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savingsGoalService.createSavingsGoal(dto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody SavingsGoalRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(savingsGoalService.updateSavingsGoal(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        savingsGoalService.deleteSavingsGoal(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
