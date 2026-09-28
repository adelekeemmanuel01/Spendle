package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.BudgetRequestDTO;
import com.emmyscode.spendle.dtos.BudgetResponseDTO;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.Budget;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<BudgetResponseDTO> getAllBudgets(User currentUser) {
        return budgetRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public BudgetResponseDTO getBudgetById(UUID id, User currentUser) {
        return budgetRepository.findByIdAndUserId(id, currentUser.getId())
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Budget not found with id: " + id));
    }

    @Transactional
    public BudgetResponseDTO createBudget(BudgetRequestDTO dto, User currentUser) {
        Budget budget = new Budget();
        budget.setCategory(dto.category());
        budget.setAmount(dto.amount());
        budget.setUser(currentUser);
        return toDto(budgetRepository.save(budget));
    }

    @Transactional
    public BudgetResponseDTO updateBudget(UUID id, BudgetRequestDTO dto, User currentUser) {
        Budget budget = budgetRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Budget not found with id: " + id));
        budget.setCategory(dto.category());
        budget.setAmount(dto.amount());
        return toDto(budgetRepository.save(budget));
    }

    @Transactional
    public void deleteBudget(UUID id, User currentUser) {
        Budget budget = budgetRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Budget not found with id: " + id));
        budgetRepository.delete(budget);
    }

    private BudgetResponseDTO toDto(Budget b) {
        return new BudgetResponseDTO(b.getId(), b.getCategory(), b.getAmount());
    }
}
