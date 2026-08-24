package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dto.SavingsGoalRequestDTO;
import com.emmyscode.spendle.dto.SavingsGoalResponseDTO;
import com.emmyscode.spendle.service.SavingsGoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/savings-goals")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping
    public ResponseEntity<SavingsGoalResponseDTO> createSavingsGoal(@RequestBody SavingsGoalRequestDTO requestDTO) {
        SavingsGoalResponseDTO createdGoal = savingsGoalService.createSavingsGoal(requestDTO);
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponseDTO> getSavingsGoalById(@PathVariable UUID id) {
        return ResponseEntity.ok(savingsGoalService.getSavingsGoalById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavingsGoalResponseDTO>> getSavingsGoalsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(savingsGoalService.getSavingsGoalsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponseDTO> updateSavingsGoal(@PathVariable UUID id, @RequestBody SavingsGoalRequestDTO requestDTO) {
        return ResponseEntity.ok(savingsGoalService.updateSavingsGoal(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavingsGoal(@PathVariable UUID id) {
        savingsGoalService.deleteSavingsGoal(id);
        return ResponseEntity.noContent().build();
    }
}
