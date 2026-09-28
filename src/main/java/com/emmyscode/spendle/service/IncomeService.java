package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.IncomeRequestDTO;
import com.emmyscode.spendle.dtos.IncomeResponseDTO;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.Income;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public List<IncomeResponseDTO> getAllIncome(User currentUser) {
        return incomeRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public IncomeResponseDTO getIncomeById(UUID id, User currentUser) {
        return incomeRepository.findByIdAndUserId(id, currentUser.getId())
                .map(this::toDto)
                // 404 regardless of whether the record exists but belongs to another user
                .orElseThrow(() -> new ResourceNotFoundException("Income not found with id: " + id));
    }

    @Transactional
    public IncomeResponseDTO createIncome(IncomeRequestDTO dto, User currentUser) {
        Income income = new Income();
        income.setAmount(dto.amount());
        income.setType(dto.type());
        income.setUser(currentUser); // server-side, never from client
        Income saved = incomeRepository.save(income);
        return toDto(saved);
    }

    @Transactional
    public IncomeResponseDTO updateIncome(UUID id, IncomeRequestDTO dto, User currentUser) {
        Income income = incomeRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Income not found with id: " + id));
        income.setAmount(dto.amount());
        income.setType(dto.type());
        return toDto(incomeRepository.save(income));
    }

    @Transactional
    public void deleteIncome(UUID id, User currentUser) {
        Income income = incomeRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Income not found with id: " + id));
        incomeRepository.delete(income);
    }

    private IncomeResponseDTO toDto(Income income) {
        return new IncomeResponseDTO(income.getId(), income.getAmount(), income.getType());
    }
}
