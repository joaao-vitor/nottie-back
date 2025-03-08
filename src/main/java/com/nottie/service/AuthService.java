package com.nottie.service;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.request.auth.LoginDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.dto.response.auth.LoggedDTO;
import com.nottie.dto.response.auth.UserLoggedDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.mapper.UserMapper;
import com.nottie.model.User;
import com.nottie.model.UserDetail;
import com.nottie.repository.UserDetailRepository;
import com.nottie.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder, UserDetailRepository userDetailRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailRepository = userDetailRepository;
    }

    public CreatedUserDTO createUser(CreateUserDTO createUserDTO) {
        logger.info("Creating user {}", createUserDTO.username());

        if (!createUserDTO.password().equals(createUserDTO.confirmPassword()))
            throw new BadRequestException("Passwords don't match");

        if(userRepository.findByEmail(createUserDTO.email()).isPresent())
            throw new BadRequestException("Email already exists");

        if(userRepository.findByUsername(createUserDTO.username()).isPresent())
            throw new BadRequestException("Username already exists");

        User user = UserMapper.INSTANCE.createUserDTOToUser(createUserDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        UserDetail userDetail = new UserDetail(user, true);
        userDetailRepository.save(userDetail);

        return UserMapper.INSTANCE.userToCreatedUserDTO(user);
    }

    public LoggedDTO authenticate(LoginDTO loginDTO) {
        logger.info("Logging in user {}", loginDTO.email());
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );
        User user = ((UserDetail) authentication.getPrincipal()).getUser();
        String token = jwtService.generateToken(authentication);

        UserLoggedDTO userLoggedDTO = UserMapper.INSTANCE.userToUserLoggedDTO(user);

        return new LoggedDTO(token, userLoggedDTO);
    }
}
