package com.emmyscode.spendle.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Returns consistent JSON error bodies instead of Spring Security's default HTML pages.
 *
 * 401 Unauthorized  → AuthenticationEntryPoint (missing / invalid / expired token)
 * 403 Forbidden     → AccessDeniedHandler (authenticated but wrong role)
 */
@Component
public class SecurityExceptionHandlers
        implements AuthenticationEntryPoint, AccessDeniedHandler {

    // ── 401 Unauthorized ──────────────────────────────────────────────────────

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication required. Please provide a valid Bearer token.");
    }

    // ── 403 Forbidden ─────────────────────────────────────────────────────────

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        writeError(response, HttpServletResponse.SC_FORBIDDEN,
                "You do not have permission to access this resource.");
    }

    // ── Shared writer ─────────────────────────────────────────────────────────

    private void writeError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Simple hand-built JSON to avoid any ObjectMapper/Jackson classpath dependency
        String json = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"message\":\"%s\"}",
                Instant.now().toString(), status, escapeJson(message));

        response.getWriter().write(json);
    }

    /** Minimal JSON string escaping for the controlled messages we produce. */
    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}

