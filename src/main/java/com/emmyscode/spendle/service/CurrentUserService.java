package com.emmyscode.spendle.service;

import com.emmyscode.spendle.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Thin helper that extracts the authenticated {@link User} from the
 * {@link SecurityContextHolder}.
 *
 * Why a dedicated service?
 *  - Keeps SecurityContext access in one place → easy to test/mock.
 *  - Services that don't use @AuthenticationPrincipal (e.g. called from
 *    scheduled jobs, events) can still get the current user here.
 *
 * The principal is always a {@code User} entity because the
 * {@link com.emmyscode.spendle.security.JwtAuthenticationFilter} stores the
 * full entity, not just a username string.
 */
@Service
public class CurrentUserService {

    /**
     * Returns the authenticated User from the SecurityContext.
     *
     * @throws IllegalStateException if called outside an authenticated request.
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user in SecurityContext");
        }
        return (User) authentication.getPrincipal();
    }
}
