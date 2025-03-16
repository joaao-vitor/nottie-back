package com.nottie.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull(message = "E-mail can't be null")
        @NotBlank(message = "E-mail can't be blank")
        String email,
        @NotNull(message = "Password can't be null")
        @NotBlank(message = "Password can't be blank")
        String password) {
}
