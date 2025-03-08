package com.nottie.service;

import com.nottie.config.JwtConfig;
import com.nottie.model.User;
import com.nottie.model.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtConfig jwtConfig, JwtEncoder jwtEncoder) {
        this.jwtConfig = jwtConfig;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        var roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        var builder = JwtClaimsSet.builder()
                .issuer("nottie-back")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtConfig.getExpirationTime()));

        User user = ((UserDetail) authentication.getPrincipal()).getUser();

        builder.subject(user.getEmail());
        builder.claim("roles", roles);
        builder.claim("username", user.getUsername());
        builder.claim("id", user.getId());

        var claims = builder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
