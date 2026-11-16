package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.AuthResponseDTO;
import com.emmyscode.spendle.dtos.LoginRequestDTO;
import com.emmyscode.spendle.dtos.SignupRequest;
import com.emmyscode.spendle.exception.EmailAlreadyExistsException;
import com.emmyscode.spendle.exception.EmailNotVerifiedException;
import com.emmyscode.spendle.exception.InvalidCredentialsException;
import com.emmyscode.spendle.model.PendingRegistration;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.PendingRegistrationRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import com.emmyscode.spendle.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Handles user registration and login.
 *
 * Security rules enforced here:
 *  - Existing verified accounts reject duplicate emails.
 *  - Unverified signups are NOT saved to the users table until the 6-digit code is confirmed.
 *  - Passwords are stored as BCrypt hashes; plaintext is never persisted or logged.
 *  - Login failures produce the same generic message unless email is pending verification (403).
 *  - Unverified users are blocked from logging in.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PendingRegistrationRepository pendingRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService verificationService;

    // ── Signup ────────────────────────────────────────────────────────────────

    @Transactional
    public void signup(SignupRequest dto) {
        String cleanEmail = dto.email().trim().toLowerCase();

        // 1. If user already exists in the users table, reject as duplicate
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        // 2. Store in pending registrations and send Brevo code.
        // NOTE: Nothing is written to the users table here!
        verificationService.registerPendingUser(
                cleanEmail,
                dto.fullName().trim(),
                dto.phoneNumber().trim(),
                passwordEncoder.encode(dto.password())
        );

        log.info("Pending registration created for {}", cleanEmail);
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public AuthResponseDTO login(LoginRequestDTO dto) {
        String email = dto.email().trim().toLowerCase();

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, dto.password()));
            } catch (BadCredentialsException ex) {
                throw new InvalidCredentialsException();
            }

            if (!user.isEmailVerified()) {
                throw new EmailNotVerifiedException();
            }

            log.info("User logged in: id={}", user.getId());
            return issueAuthResponse(user);
        }

        // If not found in users table, check if there's a pending registration
        Optional<PendingRegistration> pendingOpt = pendingRepo.findByEmailIgnoreCase(email);
        if (pendingOpt.isPresent()) {
            PendingRegistration pending = pendingOpt.get();
            if (passwordEncoder.matches(dto.password(), pending.getPasswordHash())) {
                // Correct password for an account that has not been verified yet
                throw new EmailNotVerifiedException();
            }
        }

        // Unknown email or wrong credentials
        throw new InvalidCredentialsException();
    }

    // ── Used by the verify-email endpoint ─────────────────────────────────────

    public AuthResponseDTO issueAuthResponse(User user) {
        return toAuthResponse(user, jwtService.generateToken(user));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AuthResponseDTO toAuthResponse(User user, String token) {
        return new AuthResponseDTO(user.getId(), user.getFullName(), user.getEmail(), token);
    }
}
