package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.RecurringExpenseRequestDTO;
import com.emmyscode.spendle.dtos.RecurringExpenseResponseDTO;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.RecurringExpense;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.RecurringExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;

    public List<RecurringExpenseResponseDTO> getAllRecurringExpenses(User currentUser) {
        return recurringExpenseRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public RecurringExpenseResponseDTO getRecurringExpenseById(UUID id, User currentUser) {
        return recurringExpenseRepository.findByIdAndUserId(id, currentUser.getId())
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recurring expense not found with id: " + id));
    }

    @Transactional
    public RecurringExpenseResponseDTO createRecurringExpense(
            RecurringExpenseRequestDTO dto, User currentUser) {
        RecurringExpense expense = new RecurringExpense();
        expense.setName(dto.name());
        expense.setAmount(dto.amount());
        expense.setNextPaymentDate(dto.nextPaymentDate());
        expense.setUser(currentUser);
        return toDto(recurringExpenseRepository.save(expense));
    }

    @Transactional
    public RecurringExpenseResponseDTO updateRecurringExpense(
            UUID id, RecurringExpenseRequestDTO dto, User currentUser) {
        RecurringExpense expense = recurringExpenseRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recurring expense not found with id: " + id));
        expense.setName(dto.name());
        expense.setAmount(dto.amount());
        expense.setNextPaymentDate(dto.nextPaymentDate());
        return toDto(recurringExpenseRepository.save(expense));
    }

    @Transactional
    public void deleteRecurringExpense(UUID id, User currentUser) {
        RecurringExpense expense = recurringExpenseRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recurring expense not found with id: " + id));
        recurringExpenseRepository.delete(expense);
    }

    private RecurringExpenseResponseDTO toDto(RecurringExpense e) {
        return new RecurringExpenseResponseDTO(
                e.getId(), e.getName(), e.getAmount(), e.getNextPaymentDate());
    }
}
