package com.nottie.dto.request.user;

public record ChangePasswordDTO (
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
