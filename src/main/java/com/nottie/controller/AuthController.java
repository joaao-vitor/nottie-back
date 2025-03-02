package com.nottie.controller;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.service.AuthService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserDTO createUserDTO){
        CreatedUserDTO createdUserDTO = authService.createUser(createUserDTO);
        return ResponseUtil.buildSuccessResponse(createdUserDTO, "User created successfully", HttpStatus.CREATED);
    }
}
