package com.emmyscode.spendle.security;

import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads a User from the database by email.
 *
 * Spring Security calls loadUserByUsername(email) internally during
 * AuthenticationManager.authenticate(). The User entity itself implements
 * UserDetails, so we simply return the JPA entity directly.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user found with email: " + email));
    }

    /** Convenience method for the JWT filter, which already has the email. */
    public User loadUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user found with email: " + email));
    }
}
