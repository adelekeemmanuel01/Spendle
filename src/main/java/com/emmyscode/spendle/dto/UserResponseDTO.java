package com.emmyscode.spendle.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDate registeredDate
) {
}
