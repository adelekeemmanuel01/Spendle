package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dto.BudgetRequestDTO;
import com.emmyscode.spendle.dto.BudgetResponseDTO;
import com.emmyscode.spendle.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> createBudget(@RequestBody BudgetRequestDTO requestDTO) {
        BudgetResponseDTO createdBudget = budgetService.createBudget(requestDTO);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> getBudgetById(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetResponseDTO>> getBudgetsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(budgetService.getBudgetsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> updateBudget(@PathVariable UUID id, @RequestBody BudgetRequestDTO requestDTO) {
        return ResponseEntity.ok(budgetService.updateBudget(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable UUID id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
