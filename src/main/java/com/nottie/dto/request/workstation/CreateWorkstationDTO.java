package com.nottie.dto.request.workstation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWorkstationDTO(
        @NotNull(message = "Name can't be null")
        @NotBlank(message = "Name can't be blank")
        String name,
        @Size(min = 5, max = 60, message = "Username must have between 5 to 60 characters")
        String username
) {}
