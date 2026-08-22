package com.emmyscode.spendle.service.impl;

import com.emmyscode.spendle.dto.SavingsGoalRequestDTO;
import com.emmyscode.spendle.dto.SavingsGoalResponseDTO;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.SavingsGoal;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.SavingsGoalRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import com.emmyscode.spendle.service.SavingsGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SavingsGoalServiceImpl implements SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final UserRepository userRepository;

    @Override
    public SavingsGoalResponseDTO createSavingsGoal(SavingsGoalRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.userId()));

        SavingsGoal savingsGoal = new SavingsGoal();
        savingsGoal.setName(requestDTO.name());
        savingsGoal.setTargetAmount(requestDTO.targetAmount());
        savingsGoal.setCurrentAmount(requestDTO.currentAmount());
        savingsGoal.setTargetDate(requestDTO.targetDate());
        savingsGoal.setUser(user);

        SavingsGoal savedGoal = savingsGoalRepository.save(savingsGoal);
        return mapToResponseDTO(savedGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public SavingsGoalResponseDTO getSavingsGoalById(UUID id) {
        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found with id: " + id));
        return mapToResponseDTO(savingsGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavingsGoalResponseDTO> getSavingsGoalsByUserId(UUID userId) {
        return savingsGoalRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public SavingsGoalResponseDTO updateSavingsGoal(UUID id, SavingsGoalRequestDTO requestDTO) {
        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found with id: " + id));

        savingsGoal.setName(requestDTO.name());
        savingsGoal.setTargetAmount(requestDTO.targetAmount());
        savingsGoal.setCurrentAmount(requestDTO.currentAmount());
        savingsGoal.setTargetDate(requestDTO.targetDate());

        SavingsGoal updatedGoal = savingsGoalRepository.save(savingsGoal);
        return mapToResponseDTO(updatedGoal);
    }

    @Override
    public void deleteSavingsGoal(UUID id) {
        if (!savingsGoalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Savings goal not found with id: " + id);
        }
        savingsGoalRepository.deleteById(id);
    }

    private SavingsGoalResponseDTO mapToResponseDTO(SavingsGoal savingsGoal) {
        return new SavingsGoalResponseDTO(
                savingsGoal.getId(),
                savingsGoal.getName(),
                savingsGoal.getTargetAmount(),
                savingsGoal.getCurrentAmount(),
                savingsGoal.getTargetDate(),
                savingsGoal.getUser().getId()
        );
    }
}
