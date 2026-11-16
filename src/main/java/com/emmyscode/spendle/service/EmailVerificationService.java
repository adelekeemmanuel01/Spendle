package com.emmyscode.spendle.service;

import com.emmyscode.spendle.exception.InvalidCodeException;
import com.emmyscode.spendle.exception.TooManyRequestsException;
import com.emmyscode.spendle.model.PendingRegistration;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.PendingRegistrationRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmailVerificationService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Duration CODE_TTL = Duration.ofMinutes(10);
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);
    private static final int MAX_ATTEMPTS = 5;

    private final PendingRegistrationRepository pendingRepo;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public EmailVerificationService(PendingRegistrationRepository pendingRepo,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    ApplicationEventPublisher eventPublisher) {
        this.pendingRepo = pendingRepo;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void registerPendingUser(String email, String fullName, String phoneNumber, String passwordHash) {
        String cleanEmail = email.trim().toLowerCase();

        PendingRegistration pending = pendingRepo.findByEmailIgnoreCase(cleanEmail)
                .orElseGet(() -> {
                    PendingRegistration pr = new PendingRegistration();
                    pr.setEmail(cleanEmail);
                    return pr;
                });

        pending.setFullName(fullName);
        pending.setPhoneNumber(phoneNumber);
        pending.setPasswordHash(passwordHash);

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        pending.setCodeHash(passwordEncoder.encode(code));
        pending.setExpiresAt(Instant.now().plus(CODE_TTL));
        pending.setCreatedAt(Instant.now());
        pending.setAttempts(0);

        pendingRepo.save(pending);

        eventPublisher.publishEvent(new VerificationCodeIssued(cleanEmail, code));
    }

    @Transactional
    public void resend(String rawEmail) {
        String email = rawEmail.trim().toLowerCase();

        pendingRepo.findByEmailIgnoreCase(email).ifPresent(pending -> {
            if (pending.getCreatedAt().plus(RESEND_COOLDOWN).isAfter(Instant.now())) {
                throw new TooManyRequestsException("Please wait a minute before requesting another code");
            }

            String code = String.format("%06d", RANDOM.nextInt(1_000_000));
            pending.setCodeHash(passwordEncoder.encode(code));
            pending.setExpiresAt(Instant.now().plus(CODE_TTL));
            pending.setCreatedAt(Instant.now());
            pending.setAttempts(0);
            pendingRepo.save(pending);

            eventPublisher.publishEvent(new VerificationCodeIssued(email, code));
        });
    }

    @Transactional(noRollbackFor = InvalidCodeException.class)
    public User verify(String rawEmail, String code) {
        String email = rawEmail.trim().toLowerCase();

        // If user already exists in `users` (e.g. legacy verified accounts)
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent() && existing.get().isEmailVerified()) {
            return existing.get();
        }

        PendingRegistration pending = pendingRepo.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCodeException::new);

        if (pending.getExpiresAt().isBefore(Instant.now()) || pending.getAttempts() >= MAX_ATTEMPTS) {
            throw new InvalidCodeException();
        }

        if (!passwordEncoder.matches(code, pending.getCodeHash())) {
            pending.setAttempts(pending.getAttempts() + 1);
            pendingRepo.save(pending);
            throw new InvalidCodeException();
        }

        // Code matches! NOW AND ONLY NOW, create and store the user in the database.
        User user = new User();
        user.setFullName(pending.getFullName());
        user.setEmail(pending.getEmail());
        user.setPhoneNumber(pending.getPhoneNumber());
        user.setPassword(pending.getPasswordHash());
        user.setRegisteredDate(LocalDate.now());
        user.setEmailVerified(true);

        User savedUser = userRepository.save(user);

        // Delete from pending registrations table
        pendingRepo.delete(pending);

        return savedUser;
    }
}
