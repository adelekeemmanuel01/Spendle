package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.UserResponseDTO;
import com.emmyscode.spendle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * GET /api/users/me
 * Returns profile details for the authenticated user.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String initials = "";
        if (currentUser.getFullName() != null && !currentUser.getFullName().isBlank()) {
            initials = Arrays.stream(currentUser.getFullName().trim().split("\\s+"))
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.substring(0, 1).toUpperCase())
                    .collect(Collectors.joining());
            if (initials.length() > 2) {
                initials = initials.substring(0, 2);
            }
        }
        return ResponseEntity.ok(new UserResponseDTO(
                currentUser.getId(),
                currentUser.getFullName(),
                currentUser.getEmail(),
                currentUser.getPhoneNumber(),
                initials.isEmpty() ? "??" : initials
        ));
    }
}



