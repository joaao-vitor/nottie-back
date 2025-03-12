package com.nottie.security;

import com.nottie.config.JwtConfig;
import com.nottie.model.User;
import com.nottie.model.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtService(JwtConfig jwtConfig, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtConfig = jwtConfig;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtConfig.getAccessExpirationTime());
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, jwtConfig.getRefreshExpirationTime());
    }

    private String generateToken(Authentication authentication, long validityInSeconds) {
        Instant now = Instant.now();

        var roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        var builder = JwtClaimsSet.builder()
                .issuer("nottie-back")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(validityInSeconds));

        User user = ((UserDetail) authentication.getPrincipal()).getUser();

        builder.subject(user.getEmail());
        builder.claim("roles", roles);
        builder.claim("username", user.getUsername());
        builder.claim("id", user.getId());

        var claims = builder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token) {
        return jwtDecoder.decode(token).getExpiresAt().isAfter(Instant.now());
    }

    public String extractEmailFromToken(String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
