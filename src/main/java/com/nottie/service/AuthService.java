package com.nottie.service;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.mapper.UserMapper;
import com.nottie.model.User;
import com.nottie.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public CreatedUserDTO createUser(CreateUserDTO createUserDTO) {
        logger.info("Creating user {}", createUserDTO.username());

        if(!createUserDTO.password().equals(createUserDTO.confirmPassword()))
            throw new BadRequestException("Passwords don't match");

        User user = UserMapper.INSTANCE.createUserDTOToUser(createUserDTO);

        userRepository.save(user);

        return UserMapper.INSTANCE.userToCreatedUserDTO(user);
    }
}
