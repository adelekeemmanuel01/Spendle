package com.emmyscode.spendle.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralised error handling for all controllers.
 *
 * Response shape:
 *   { "timestamp": "...", "status": 4xx/5xx, "message": "..." }
 *
 * Validation errors (400) add a "errors" map:
 *   { ..., "errors": { "fieldName": "validation message" } }
 *
 * Stack traces are NEVER sent to the client. Unexpected errors are logged
 * server-side and a generic message is returned.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 400 Bad Request – Bean Validation failures ────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorBody(400, "Validation failed", fieldErrors));
    }

    // ── 401 Unauthorized – bad credentials ───────────────────────────────────

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorBody(401, ex.getMessage()));
    }

    // ── 404 Not Found ─────────────────────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorBody(404, ex.getMessage()));
    }

    // ── 409 Conflict – duplicate email ────────────────────────────────────────

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
            EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorBody(409, ex.getMessage()));
    }

    // ── 409 Conflict – generic duplicate resource ─────────────────────────────

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(
            DuplicateResourceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorBody(409, ex.getMessage()));
    }

    // ── 400 Bad Request – Malformed JSON ─────────────────────────────────────

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        log.debug("Malformed JSON request", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorBody(400, "Malformed JSON request. Please check your request body format."));
    }

    // ── 500 Internal Server Error – catch-all ────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex); // full stack trace in server logs only
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody(500, "An unexpected error occurred. Please try again later."));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, Object> errorBody(int status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status);
        body.put("message", message);
        return body;
    }

    private Map<String, Object> errorBody(int status, String message,
                                          Map<String, String> errors) {
        Map<String, Object> body = errorBody(status, message);
        body.put("errors", errors);
        return body;
    }
}
