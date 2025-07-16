package com.nottie.dto.response.user;

public record SearchUserDTO(
        Long id,
        String name,
        String username,
        String profileImg
) {
}
