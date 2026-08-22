package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dto.IncomeRequestDTO;
import com.emmyscode.spendle.dto.IncomeResponseDTO;
import com.emmyscode.spendle.enums.IncomeType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IncomeService {

    IncomeResponseDTO createIncome(IncomeRequestDTO requestDTO);

    IncomeResponseDTO getIncomeById(UUID id);

    List<IncomeResponseDTO> getIncomesByUserId(UUID userId);

//    List<IncomeResponseDTO> getIncomesByUserIdAndType(UUID userId, IncomeType type);

//    List<IncomeResponseDTO> getIncomesByUserIdAndDateRange(UUID userId, LocalDate start, LocalDate end);

    IncomeResponseDTO updateIncome(UUID id, IncomeRequestDTO requestDTO);

    void deleteIncome(UUID id);
}
