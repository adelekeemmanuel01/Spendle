package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dto.SavingsGoalRequestDTO;
import com.emmyscode.spendle.dto.SavingsGoalResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SavingsGoalService {

    SavingsGoalResponseDTO createSavingsGoal(SavingsGoalRequestDTO requestDTO);

    SavingsGoalResponseDTO getSavingsGoalById(UUID id);

    List<SavingsGoalResponseDTO> getSavingsGoalsByUserId(UUID userId);

    SavingsGoalResponseDTO updateSavingsGoal(UUID id, SavingsGoalRequestDTO requestDTO);

    void deleteSavingsGoal(UUID id);
}
