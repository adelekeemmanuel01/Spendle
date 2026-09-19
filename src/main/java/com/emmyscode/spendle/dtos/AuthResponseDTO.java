package com.emmyscode.spendle.dtos;

import java.util.UUID;

public record AuthResponseDTO(
        UUID userId,
        String fullName,
        String email,
        String token
) {
}
