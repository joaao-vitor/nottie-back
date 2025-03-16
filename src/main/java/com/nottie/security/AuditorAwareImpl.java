package com.nottie.security;

import com.nottie.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
public class AuditorAwareImpl implements AuditorAware<User> {
    public AuditorAwareImpl() {
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
        User user = new User();
        user.setEmail(email);
        user.setId(jwt.getClaim("id"));
        user.setUsername(jwt.getClaim("username"));
        return Optional.of(user);
    }
}
