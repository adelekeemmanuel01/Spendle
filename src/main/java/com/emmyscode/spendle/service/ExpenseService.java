package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dto.ExpenseRequestDTO;
import com.emmyscode.spendle.dto.ExpenseResponseDTO;
import com.emmyscode.spendle.enums.Categories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO);

    ExpenseResponseDTO getExpenseById(UUID id);

    List<ExpenseResponseDTO> getExpensesByUserId(UUID userId);

//    List<ExpenseResponseDTO> getExpensesByUserIdAndCategory(UUID userId, Categories category);
//
//    List<ExpenseResponseDTO> getExpensesByUserIdAndDateRange(UUID userId, LocalDateTime start, LocalDateTime end);

    ExpenseResponseDTO updateExpense(UUID id, ExpenseRequestDTO requestDTO);

    void deleteExpense(UUID id);
}
