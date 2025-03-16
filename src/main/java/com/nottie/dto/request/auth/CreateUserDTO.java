package com.nottie.dto.request.auth;

import jakarta.validation.constraints.*;

public record CreateUserDTO(
        @NotNull(message = "Username can't be null")
        @NotBlank(message = "Username can't be blank")
        @Size(min = 5, max = 60, message = "Username must have between 5 to 60 characters")
        String username,
        @Email(message = "Email is not valid")
        String email,
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "The password must have at least 8 characters, one special symbol and one uppercase letter")
        String password,
        @Size(min = 8, message = "Confirm Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "The password must have at least 8 characters, one special symbol and one uppercase letter")
        String confirmPassword,
        @NotBlank(message = "Name can't be blank")
        @NotNull(message = "Name can't be null")
        String name
) {
}
