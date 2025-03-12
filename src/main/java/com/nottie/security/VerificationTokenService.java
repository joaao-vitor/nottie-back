package com.nottie.security;

import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.model.User;
import com.nottie.model.UserDetail;
import com.nottie.model.VerificationToken;
import com.nottie.repository.UserDetailRepository;
import com.nottie.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class VerificationTokenService {
    private final UserDetailRepository userDetailRepository;
    @Value("${security.email.verification.expiration-time}")
    private long expirationTime;

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository, UserDetailRepository userDetailRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userDetailRepository = userDetailRepository;
    }

    public VerificationToken createVerificationToken(User user) {
        Instant now = Instant.now();
        LocalDateTime expirationDate = LocalDateTime.ofInstant(now.plusSeconds(expirationTime), ZoneId.systemDefault());

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(expirationDate);

        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }

    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Token not found"));

        UserDetail userDetail = userDetailRepository.findByEmail(verificationToken.getUser().getEmail()).orElseThrow(() -> new NotFoundException("User not found"));

        if(verificationToken.isVerified()) throw new BadRequestException("Token is already verified");

        if(verificationToken.isExpired() || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationToken.setExpired(true);
            verificationTokenRepository.save(verificationToken);
            throw new UnauthorizedException("Token is expired");
        }

        verificationToken.setVerified(true);
        verificationToken.setExpired(true);
        verificationTokenRepository.save(verificationToken);

        userDetail.setEnabled(true);
        userDetailRepository.save(userDetail);
    }
}

