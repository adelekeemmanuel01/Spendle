package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.IncomeRequestDTO;
import com.emmyscode.spendle.dtos.IncomeResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for Income records.
 * All operations are scoped to the authenticated user — no userId in URLs or bodies.
 */
@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> getAllIncome(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(incomeService.getAllIncome(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> getIncomeById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(incomeService.getIncomeById(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> createIncome(
            @Valid @RequestBody IncomeRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(incomeService.createIncome(dto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> updateIncome(
            @PathVariable UUID id,
            @Valid @RequestBody IncomeRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(incomeService.updateIncome(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        incomeService.deleteIncome(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
