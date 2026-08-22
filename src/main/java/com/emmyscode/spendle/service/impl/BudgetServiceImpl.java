package com.emmyscode.spendle.service.impl;

import com.emmyscode.spendle.dto.BudgetRequestDTO;
import com.emmyscode.spendle.dto.BudgetResponseDTO;
import com.emmyscode.spendle.enums.Categories;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.Budget;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.BudgetRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import com.emmyscode.spendle.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Override
    public BudgetResponseDTO createBudget(BudgetRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.userId()));

        Budget budget = new Budget();
        budget.setCategory(requestDTO.category());
        budget.setAmount(requestDTO.amount());
        budget.setStartDate(requestDTO.startDate());
        budget.setEndDate(requestDTO.endDate());
        budget.setUser(user);

        Budget savedBudget = budgetRepository.save(budget);
        return mapToResponseDTO(savedBudget);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponseDTO getBudgetById(UUID id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
        return mapToResponseDTO(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetResponseDTO> getBudgetsByUserId(UUID userId) {
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<BudgetResponseDTO> getBudgetsByUserIdAndCategory(UUID userId, Categories category) {
//        return budgetRepository.findByUserIdAndCategory(userId, category)
//                .stream()
//                .map(this::mapToResponseDTO)
//                .toList();
//    }

    @Override
    public BudgetResponseDTO updateBudget(UUID id, BudgetRequestDTO requestDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));

        budget.setCategory(requestDTO.category());
        budget.setAmount(requestDTO.amount());
        budget.setStartDate(requestDTO.startDate());
        budget.setEndDate(requestDTO.endDate());

        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponseDTO(updatedBudget);
    }

    @Override
    public void deleteBudget(UUID id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Budget not found with id: " + id);
        }
        budgetRepository.deleteById(id);
    }

    private BudgetResponseDTO mapToResponseDTO(Budget budget) {
        return new BudgetResponseDTO(
                budget.getId(),
                budget.getCategory(),
                budget.getAmount(),
                budget.getStartDate(),
                budget.getEndDate(),
                budget.getUser().getId()
        );
    }
}
