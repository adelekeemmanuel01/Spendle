package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dto.BudgetRequestDTO;
import com.emmyscode.spendle.dto.BudgetResponseDTO;
import com.emmyscode.spendle.enums.Categories;

import java.util.List;
import java.util.UUID;

public interface BudgetService {

    BudgetResponseDTO createBudget(BudgetRequestDTO requestDTO);

    BudgetResponseDTO getBudgetById(UUID id);

    List<BudgetResponseDTO> getBudgetsByUserId(UUID userId);

//    List<BudgetResponseDTO> getBudgetsByUserIdAndCategory(UUID userId, Categories category);

    BudgetResponseDTO updateBudget(UUID id, BudgetRequestDTO requestDTO);

    void deleteBudget(UUID id);
}
