package com.emmyscode.spendle.service;

import com.emmyscode.spendle.exception.InvalidCodeException;
import com.emmyscode.spendle.exception.TooManyRequestsException;
import com.emmyscode.spendle.model.EmailVerificationCode;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.EmailVerificationCodeRepository;
import com.emmyscode.spendle.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
public class EmailVerificationService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Duration CODE_TTL = Duration.ofMinutes(10);
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);
    private static final int MAX_ATTEMPTS = 5;

    private final EmailVerificationCodeRepository codeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public EmailVerificationService(EmailVerificationCodeRepository codeRepository,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    ApplicationEventPublisher eventPublisher) {
        this.codeRepository = codeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void issueCode(User user) {
        codeRepository.findTopByUserOrderByCreatedAtDesc(user).ifPresent(last -> {
            if (last.getCreatedAt().plus(RESEND_COOLDOWN).isAfter(Instant.now())) {
                throw new TooManyRequestsException("Please wait a minute before requesting another code");
            }
        });

        codeRepository.deleteByUser(user);

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));

        EmailVerificationCode entity = new EmailVerificationCode();
        entity.setUser(user);
        entity.setCodeHash(passwordEncoder.encode(code));
        entity.setExpiresAt(Instant.now().plus(CODE_TTL));
        codeRepository.save(entity);

        eventPublisher.publishEvent(new VerificationCodeIssued(user.getEmail(), code));
    }

    @Transactional
    public void resend(String email) {
        userRepository.findByEmail(email)
                .filter(user -> !user.isEmailVerified())
                .ifPresent(this::issueCode);
    }

    @Transactional(noRollbackFor = InvalidCodeException.class)
    public User verify(String email, String code) {
        User user = userRepository.findByEmail(email).orElseThrow(InvalidCodeException::new);

        if (user.isEmailVerified()) {
            return user;
        }

        EmailVerificationCode otp = codeRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElseThrow(InvalidCodeException::new);

        if (otp.getExpiresAt().isBefore(Instant.now()) || otp.getAttempts() >= MAX_ATTEMPTS) {
            throw new InvalidCodeException();
        }

        if (!passwordEncoder.matches(code, otp.getCodeHash())) {
            otp.setAttempts(otp.getAttempts() + 1);
            throw new InvalidCodeException();
        }

        user.setEmailVerified(true);
        codeRepository.delete(otp);
        return user;
    }

}
