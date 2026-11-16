package com.emmyscode.spendle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

    private static final Logger log = LoggerFactory.getLogger(BrevoEmailService.class);

    private final RestClient client;
    private final String senderEmail;
    private final String senderName;

    public BrevoEmailService(@Value("${brevo.api-key}") String apiKey,
                             @Value("${brevo.sender-email}") String senderEmail,
                             @Value("${brevo.sender-name}") String senderName) {
        this.client = RestClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", apiKey)
                .defaultHeader("accept", "application/json")
                .build();
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    public void sendVerificationCode(String toEmail, String code) {
        Map<String, Object> body = Map.of(
                "sender", Map.of("name", senderName, "email", senderEmail),
                "to", List.of(Map.of("email", toEmail)),
                "subject", "Your Spendle verification code",
                "htmlContent", buildHtml(code)
        );

        try {
            client.post()
                    .uri("/smtp/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            log.error("Brevo rejected the email: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    private String buildHtml(String code) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:420px;margin:auto">
              <h2 style="color:#05585E">Confirm your email</h2>
              <p>Enter this code in Spendle to finish signing up:</p>
              <p style="font-size:32px;letter-spacing:8px;font-weight:bold;color:#0E2036">%s</p>
              <p>The code expires in 10 minutes. If you didn't create a Spendle account, ignore this email.</p>
            </div>
            """.formatted(code);
    }

}
