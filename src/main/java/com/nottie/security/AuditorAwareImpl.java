package com.nottie.security;

import com.nottie.model.User;
import com.nottie.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    private final UserRepository userRepository;

    public AuditorAwareImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
            return Optional.empty();

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getSubject();

        if(email.isBlank())
            return Optional.empty();

        return userRepository.findByEmailIgnoreCase(email);
    }
}
