package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.service.BrevoEmailService;
import com.emmyscode.spendle.service.VerificationCodeIssued;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationMailListener {

    private final BrevoEmailService brevoEmailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCodeIssued(VerificationCodeIssued event) {
        log.info("Sending verification email to {}", event.email());
        try {
            brevoEmailService.sendVerificationCode(event.email(), event.code());
            log.info("Brevo accepted the verification email for {}", event.email());
        } catch (Exception ex) {
            log.error("Verification email failed for {}", event.email(), ex);
        }
    }

}
