package com.nottie.controller;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.request.auth.LoginDTO;
import com.nottie.dto.request.auth.RefreshTokenRequestDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.dto.response.auth.LoggedDTO;
import com.nottie.dto.response.auth.RefreshTokenResponseDTO;
import com.nottie.service.AuthService;
import com.nottie.security.VerificationTokenService;
import com.nottie.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;

    public AuthController(AuthService authService, VerificationTokenService verificationTokenService) {
        this.authService = authService;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid CreateUserDTO createUserDTO){
        CreatedUserDTO createdUserDTO = authService.createUser(createUserDTO);
        return ResponseUtil.buildSuccessResponse(createdUserDTO, "User created successfully, verify your email", HttpStatus.CREATED);
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

    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        verificationTokenService.verifyToken(token);
        return ResponseUtil.buildSuccessResponse(null, "Email verified successfully", HttpStatus.OK);
    }
}
