package com.nottie.service;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.request.auth.LoginDTO;
import com.nottie.dto.request.auth.RefreshTokenRequestDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.dto.response.auth.LoggedDTO;
import com.nottie.dto.response.auth.RefreshTokenResponseDTO;
import com.nottie.dto.response.auth.UserLoggedDTO;
import com.nottie.exception.BadCredentialsException;
import com.nottie.exception.BadRequestException;
import com.nottie.mapper.UserMapper;
import com.nottie.model.User;
import com.nottie.model.UserDetail;
import com.nottie.model.VerificationToken;
import com.nottie.repository.UserDetailRepository;
import com.nottie.repository.UserRepository;
import com.nottie.security.JwtService;
import com.nottie.security.VerificationTokenService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${application.website.verification-link}")
    private String verificationLink;

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder, UserDetailRepository userDetailRepository, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailRepository = userDetailRepository;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @Transactional
    public CreatedUserDTO createUser(CreateUserDTO createUserDTO) {
        logger.info("Creating user {}", createUserDTO.username());

        if (!createUserDTO.password().equals(createUserDTO.confirmPassword()))
            throw new BadRequestException("As senhas não combinam");

        if(userRepository.findByEmailIgnoreCase(createUserDTO.email()).isPresent())
            throw new BadRequestException("E-mail já existente");

        if(userRepository.findByUsernameIgnoreCase(createUserDTO.username()).isPresent())
            throw new BadRequestException("Nome de usuário já existente");

        User user = UserMapper.INSTANCE.createUserDTOToUser(createUserDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);

        sendVerificationEmail(user.getEmail(), verificationToken.getToken());

        UserDetail userDetail = new UserDetail(user, false);
        userDetailRepository.save(userDetail);

        return UserMapper.INSTANCE.userToCreatedUserDTO(user);
    }

    public LoggedDTO authenticate(LoginDTO loginDTO) {
        logger.info("Logging in user {}", loginDTO.email());
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );
        User user = ((UserDetail) authentication.getPrincipal()).getUser();
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);

        UserLoggedDTO userLoggedDTO = UserMapper.INSTANCE.userToUserLoggedDTO(user);

        return new LoggedDTO(accessToken, refreshToken, userLoggedDTO);
    }

    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        if(!jwtService.validateToken(refreshTokenRequestDTO.refreshToken()))
            throw new BadCredentialsException("Refresh token inválido");

        String email = jwtService.extractEmailFromToken(refreshTokenRequestDTO.refreshToken());
        UserDetail userDetail = userDetailRepository.findByEmail(email).orElse(null);
        if(userDetail == null || !userDetail.isEnabled() || userDetail.getUser() == null)
            throw new BadCredentialsException("Refresh token inválido");


        var authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

        String newAccessToken = jwtService.generateAccessToken(authentication);

        return new RefreshTokenResponseDTO(newAccessToken);
    }

    private void sendVerificationEmail(String email, String token) {
        String link = "<a href=\"" + verificationLink + "?token="+ token + "\">" + "Clique aqui" + "</a>";

        emailService.sendSimpleMail(email, "Welcome to Nottie!",
                "<h1>Bem vindo ao Nottie!</h1>" +
                        "<h4>Confirme o seu endereço de e-mail</h4>" +
                        "<h4>Clique no link abaixo</h4>" +
                        link);
    }
}
