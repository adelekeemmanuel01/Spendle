//package com.emmyscode.spendle.controller;
//
//import com.emmyscode.spendle.dtos.AuthResponseDTO;
//import com.emmyscode.spendle.dtos.LoginRequestDTO;
//import com.emmyscode.spendle.dtos.SignupRequest;
//import com.emmyscode.spendle.service.AuthService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Public authentication endpoints.
// *
// * POST /api/auth/signup  – register + return JWT (201 Created)
// * POST /api/auth/login   – authenticate + return JWT (200 OK)
// *
// * Both endpoints are permit-all in SecurityConfig; no token required.
// * @Valid triggers Bean Validation; errors are handled by GlobalExceptionHandler.
// */
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody SignupRequest dto) {
//        AuthResponseDTO response = authService.signup(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
//        AuthResponseDTO response = authService.login(dto);
//        return ResponseEntity.ok(response);
//    }
//}

package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.AuthResponseDTO;
import com.emmyscode.spendle.dtos.LoginRequestDTO;
import com.emmyscode.spendle.dtos.ResendCodeRequest;
import com.emmyscode.spendle.dtos.SignupRequest;
import com.emmyscode.spendle.dtos.VerifyEmailRequest;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.service.AuthService;
import com.emmyscode.spendle.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Public authentication endpoints.
 *
 * POST /api/auth/signup        – register + email a 6-digit code (201 Created, no JWT)
 * POST /api/auth/verify-email  – confirm the code + return JWT (200 OK)
 * POST /api/auth/resend-code   – send a fresh code (204 No Content)
 * POST /api/auth/login         – authenticate + return JWT (200 OK, 403 if email unverified)
 *
 * All endpoints are permit-all in SecurityConfig; no token required.
 * @Valid triggers Bean Validation; errors are handled by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService verificationService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignupRequest dto) {
        authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Verification code sent", "email", dto.email()));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponseDTO> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        User user = verificationService.verify(request.email(), request.code());
        return ResponseEntity.ok(authService.issueAuthResponse(user));
    }

    @PostMapping("/resend-code")
    public ResponseEntity<Void> resendCode(@Valid @RequestBody ResendCodeRequest request) {
        verificationService.resend(request.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
