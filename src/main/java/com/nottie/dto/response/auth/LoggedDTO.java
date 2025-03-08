package com.nottie.dto.response.auth;

public record LoggedDTO(String accessToken, String refreshToken, UserLoggedDTO user) {
}
