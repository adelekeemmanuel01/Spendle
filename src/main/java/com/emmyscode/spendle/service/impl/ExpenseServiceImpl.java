package com.emmyscode.spendle.service.impl;

import com.emmyscode.spendle.dto.ExpenseRequestDTO;
import com.emmyscode.spendle.dto.ExpenseResponseDTO;
import com.emmyscode.spendle.enums.Categories;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.Expense;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.ExpenseRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import com.emmyscode.spendle.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.userId()));

        Expense expense = new Expense();
        expense.setAmount(requestDTO.amount());
        expense.setDescription(requestDTO.description());
        expense.setCategory(requestDTO.category());
        expense.setDate(requestDTO.date());
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);
        return mapToResponseDTO(savedExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDTO getExpenseById(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return mapToResponseDTO(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByUserId(UUID userId) {
        return expenseRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ExpenseResponseDTO> getExpensesByUserIdAndCategory(UUID userId, Categories category) {
//        return expenseRepository.findByUserIdAndCategory(userId, category)
//                .stream()
//                .map(this::mapToResponseDTO)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ExpenseResponseDTO> getExpensesByUserIdAndDateRange(UUID userId, LocalDateTime start, LocalDateTime end) {
//        return expenseRepository.findByUserIdAndDateBetween(userId, start, end)
//                .stream()
//                .map(this::mapToResponseDTO)
//                .toList();
//    }

    @Override
    public ExpenseResponseDTO updateExpense(UUID id, ExpenseRequestDTO requestDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        expense.setAmount(requestDTO.amount());
        expense.setDescription(requestDTO.description());
        expense.setCategory(requestDTO.category());
        expense.setDate(requestDTO.date());

        Expense updatedExpense = expenseRepository.save(expense);
        return mapToResponseDTO(updatedExpense);
    }

    @Override
    public void deleteExpense(UUID id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    private ExpenseResponseDTO mapToResponseDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getCategory(),
                expense.getDate(),
                expense.getUser().getId()
        );
    }
}
