package com.emmyscode.spendle.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public class VerificationMailListener {

    private final BrevoEmailService brevoEmailService;

    public VerificationMailListener(BrevoEmailService brevoEmailService) {
        this.brevoEmailService = brevoEmailService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCodeIssued(VerificationCodeIssued event) {
        brevoEmailService.sendVerificationCode(event.email(), event.code());
    }

}
