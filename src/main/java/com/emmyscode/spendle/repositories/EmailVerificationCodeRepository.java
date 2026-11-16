package com.emmyscode.spendle.repositories;

import com.emmyscode.spendle.model.EmailVerificationCode;
import com.emmyscode.spendle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, UUID> {
    Optional<EmailVerificationCode> findTopByUserOrderByCreatedAtDesc(User user);

    void deleteByUser(User user);
}
