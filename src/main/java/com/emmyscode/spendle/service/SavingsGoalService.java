package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.SavingsGoalRequestDTO;
import com.emmyscode.spendle.dtos.SavingsGoalResponseDTO;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.SavingsGoal;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.SavingsGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    public List<SavingsGoalResponseDTO> getAllSavingsGoals(User currentUser) {
        return savingsGoalRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public SavingsGoalResponseDTO getSavingsGoalById(UUID id, User currentUser) {
        return savingsGoalRepository.findByIdAndUserId(id, currentUser.getId())
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Savings goal not found with id: " + id));
    }

    @Transactional
    public SavingsGoalResponseDTO createSavingsGoal(SavingsGoalRequestDTO dto, User currentUser) {
        SavingsGoal goal = new SavingsGoal();
        goal.setTargetAmount(dto.targetAmount());
        goal.setMonthlyAllocation(dto.monthlyAllocation());
        goal.setCurrentAmount(dto.currentAmount() != null ? dto.currentAmount() : BigDecimal.ZERO);
        goal.setTargetDate(dto.targetDate());
        goal.setUser(currentUser);
        return toDto(savingsGoalRepository.save(goal));
    }

    @Transactional
    public SavingsGoalResponseDTO updateSavingsGoal(
            UUID id, SavingsGoalRequestDTO dto, User currentUser) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Savings goal not found with id: " + id));
        goal.setTargetAmount(dto.targetAmount());
        goal.setMonthlyAllocation(dto.monthlyAllocation());
        if (dto.currentAmount() != null) {
            goal.setCurrentAmount(dto.currentAmount());
        }
        goal.setTargetDate(dto.targetDate());
        return toDto(savingsGoalRepository.save(goal));
    }

    @Transactional
    public void deleteSavingsGoal(UUID id, User currentUser) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Savings goal not found with id: " + id));
        savingsGoalRepository.delete(goal);
    }

    private SavingsGoalResponseDTO toDto(SavingsGoal s) {
        return new SavingsGoalResponseDTO(
                s.getId(), s.getTargetAmount(),
                s.getMonthlyAllocation(), s.getCurrentAmount(), s.getTargetDate());
    }
}
