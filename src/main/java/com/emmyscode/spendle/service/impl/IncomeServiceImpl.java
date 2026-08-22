package com.emmyscode.spendle.service.impl;

import com.emmyscode.spendle.dto.IncomeRequestDTO;
import com.emmyscode.spendle.dto.IncomeResponseDTO;
import com.emmyscode.spendle.enums.IncomeType;
import com.emmyscode.spendle.exception.ResourceNotFoundException;
import com.emmyscode.spendle.model.Income;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.IncomeRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import com.emmyscode.spendle.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Override
    public IncomeResponseDTO createIncome(IncomeRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.userId()));

        Income income = new Income();
        income.setAmount(requestDTO.amount());
        income.setSource(requestDTO.source());
        income.setDate(requestDTO.date());
        income.setType(requestDTO.type());
        income.setUser(user);

        Income savedIncome = incomeRepository.save(income);
        return mapToResponseDTO(savedIncome);
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeResponseDTO getIncomeById(UUID id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found with id: " + id));
        return mapToResponseDTO(income);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeResponseDTO> getIncomesByUserId(UUID userId) {
        return incomeRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

//
//    @Override
//    @Transactional(readOnly = true)
//    public List<IncomeResponseDTO> getIncomesByUserIdAndType(UUID userId, IncomeType type) {
//        return incomeRepository.findByUserIdAndType(userId, type)
//                .stream()
//                .map(this::mapToResponseDTO)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<IncomeResponseDTO> getIncomesByUserIdAndDateRange(UUID userId, LocalDate start, LocalDate end) {
//        return incomeRepository.findByUserIdAndDateBetween(userId, start, end)
//                .stream()
//                .map(this::mapToResponseDTO)
//                .toList();
//    }

    @Override
    public IncomeResponseDTO updateIncome(UUID id, IncomeRequestDTO requestDTO) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found with id: " + id));

        income.setAmount(requestDTO.amount());
        income.setSource(requestDTO.source());
        income.setDate(requestDTO.date());
        income.setType(requestDTO.type());

        Income updatedIncome = incomeRepository.save(income);
        return mapToResponseDTO(updatedIncome);
    }

    @Override
    public void deleteIncome(UUID id) {
        if (!incomeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Income not found with id: " + id);
        }
        incomeRepository.deleteById(id);
    }

    private IncomeResponseDTO mapToResponseDTO(Income income) {
        return new IncomeResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getSource(),
                income.getDate(),
                income.getType(),
                income.getUser().getId()
        );
    }
}
