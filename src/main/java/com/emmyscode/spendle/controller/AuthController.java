package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.AuthResponseDTO;
import com.emmyscode.spendle.dtos.LoginRequestDTO;
import com.emmyscode.spendle.dtos.SignupRequest;
import com.emmyscode.spendle.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public authentication endpoints.
 *
 * POST /api/auth/signup  – register + return JWT (201 Created)
 * POST /api/auth/login   – authenticate + return JWT (200 OK)
 *
 * Both endpoints are permit-all in SecurityConfig; no token required.
 * @Valid triggers Bean Validation; errors are handled by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody SignupRequest dto) {
        AuthResponseDTO response = authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
