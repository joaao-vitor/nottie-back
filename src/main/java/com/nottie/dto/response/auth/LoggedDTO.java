package com.nottie.dto.response.auth;

public record LoggedDTO(String token, UserLoggedDTO user) {
}
