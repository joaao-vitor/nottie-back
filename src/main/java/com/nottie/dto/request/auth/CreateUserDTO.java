package com.nottie.dto.request.auth;

public record CreateUserDTO(
        String username,
        String email,
        String password,
        String confirmPassword,
        String name
) {
}
