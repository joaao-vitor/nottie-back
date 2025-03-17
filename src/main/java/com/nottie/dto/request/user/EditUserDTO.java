package com.nottie.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EditUserDTO(
        @NotEmpty(message = "Name can't be empty")
        @NotBlank(message = "Name can't be blank")
        @NotNull(message = "Name can't be null")
        String name,
        @NotNull(message = "Username can't be null")
        @NotBlank(message = "Username can't be blank")
        @Size(min = 5, max = 60, message = "Username must have between 5 to 60 characters")
        String username
) {
}
