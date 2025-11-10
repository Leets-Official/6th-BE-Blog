package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.User;

public record AuthResponse(
        String accessToken,
        String tokenType,   // "Bearer"
        String email,
        String nickname,
        String provider,
        String role
) {
    public static AuthResponse of(String accessToken, User user) {
        return new AuthResponse(accessToken, "Bearer", user.getEmail(), user.getNickname(),
                user.getProvider(), user.getRole());
    }
}
