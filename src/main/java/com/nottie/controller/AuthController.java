package com.nottie.controller;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.request.auth.LoginDTO;
import com.nottie.dto.request.auth.RefreshTokenRequestDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.dto.response.auth.LoggedDTO;
import com.nottie.dto.response.auth.RefreshTokenResponseDTO;
import com.nottie.service.AuthService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO){
        LoggedDTO loggedDTO = authService.authenticate(loginDTO);
        return ResponseUtil.buildSuccessResponse(loggedDTO, "User logged successfully", HttpStatus.OK);
    }

    @PutMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        RefreshTokenResponseDTO token = authService.refreshToken(refreshTokenRequestDTO);
        return ResponseUtil.buildSuccessResponse(token, "Refresh token successfully", HttpStatus.OK);
    }
}
