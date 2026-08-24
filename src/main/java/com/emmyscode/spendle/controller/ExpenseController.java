package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dto.ExpenseRequestDTO;
import com.emmyscode.spendle.dto.ExpenseResponseDTO;
import com.emmyscode.spendle.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody ExpenseRequestDTO requestDTO) {
        ExpenseResponseDTO createdExpense = expenseService.createExpense(requestDTO);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable UUID id, @RequestBody ExpenseRequestDTO requestDTO) {
        return ResponseEntity.ok(expenseService.updateExpense(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
