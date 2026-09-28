package com.emmyscode.spendle.security;

import com.emmyscode.spendle.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Stateless JWT utility.
 *
 * Token structure:
 *   sub   = user UUID (string)
 *   email = user email
 *   role  = e.g. "USER"
 *
 * The secret is read from the environment / application.properties (jwt.secret).
 * It must be at least 256 bits (32 ASCII characters) for HS256.
 * NEVER log the secret, tokens, or Authorization headers.
 */
@Slf4j
@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // ── Token generation ──────────────────────────────────────────────────────

    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getId().toString())       // sub = UUID
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    // ── Token extraction ──────────────────────────────────────────────────────

    public UUID extractUserId(String token) {
        return UUID.fromString(extractAllClaims(token).getSubject());
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // ── Token validation ──────────────────────────────────────────────────────

    /**
     * Returns true only if the token is well-formed, signed with our key,
     * not expired, and the subject matches the given user's id.
     */
    public boolean isTokenValid(String token, User user) {
        try {
            Claims claims = extractAllClaims(token);
            String subjectId = claims.getSubject();
            return subjectId.equals(user.getId().toString())
                    && !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
