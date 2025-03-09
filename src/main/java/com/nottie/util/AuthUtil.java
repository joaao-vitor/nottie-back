package com.nottie.util;

import com.nottie.exception.UnauthorizedException;
import com.nottie.model.User;
import com.nottie.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    private final UserRepository userRepository;
    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getAuthenticatedUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = jwt.getClaim("id");
        return userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("No user authenticated"));
    }
}
