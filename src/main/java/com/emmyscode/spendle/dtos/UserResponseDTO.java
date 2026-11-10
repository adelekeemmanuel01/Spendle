package com.emmyscode.spendle.dtos;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String fullName,
        String email,
        String phone,
        String avatarInitials
) {
}

