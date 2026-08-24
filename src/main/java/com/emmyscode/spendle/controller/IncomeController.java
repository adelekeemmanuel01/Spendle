package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dto.IncomeRequestDTO;
import com.emmyscode.spendle.dto.IncomeResponseDTO;
import com.emmyscode.spendle.service.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> createIncome(@RequestBody IncomeRequestDTO requestDTO) {
        IncomeResponseDTO createdIncome = incomeService.createIncome(requestDTO);
        return new ResponseEntity<>(createdIncome, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> getIncomeById(@PathVariable UUID id) {
        return ResponseEntity.ok(incomeService.getIncomeById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeResponseDTO>> getIncomesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(incomeService.getIncomesByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> updateIncome(@PathVariable UUID id, @RequestBody IncomeRequestDTO requestDTO) {
        return ResponseEntity.ok(incomeService.updateIncome(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable UUID id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
