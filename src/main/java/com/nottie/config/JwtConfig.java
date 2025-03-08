package com.nottie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {
    @Value("${security.jwt.public.key}")
    private RSAPublicKey publicKey;
    @Value("${security.jwt.private.key}")
    private RSAPrivateKey privateKey;
    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

}
