package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dtos.AuthResponseDTO;
import com.emmyscode.spendle.dtos.LoginRequestDTO;
import com.emmyscode.spendle.dtos.SignupRequest;
import com.emmyscode.spendle.exception.EmailAlreadyExistsException;
import com.emmyscode.spendle.exception.InvalidCredentialsException;
import com.emmyscode.spendle.model.User;
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

import java.time.LocalDate;

/**
 * Handles user registration and login.
 *
 * Security rules enforced here:
 *  - Duplicate emails are rejected before any DB write.
 *  - Passwords are stored as BCrypt hashes; plaintext is never persisted or logged.
 *  - Login failures always produce the same generic message to prevent user enumeration.
 *  - A JWT is returned on both signup and login so the React client can proceed
 *    directly to the dashboard without a second round-trip.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // ── Signup ────────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponseDTO signup(SignupRequest dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setPassword(passwordEncoder.encode(dto.password())); // hash, never plaintext
        user.setRegisteredDate(LocalDate.now());
        // role defaults to USER via the field initializer on User entity

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        log.info("New user registered: id={}", saved.getId());
        return toAuthResponse(saved, token);
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
        } catch (BadCredentialsException ex) {
            // Deliberate: same message whether email is unknown or password is wrong.
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(InvalidCredentialsException::new);

        String token = jwtService.generateToken(user);
        log.info("User logged in: id={}", user.getId());
        return toAuthResponse(user, token);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AuthResponseDTO toAuthResponse(User user, String token) {
        return new AuthResponseDTO(user.getId(), user.getFullName(), user.getEmail(), token);
    }
}
