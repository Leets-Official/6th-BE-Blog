package com.leets.backend.blog_manage.dto.auth;

/**
 * JWT 토큰 응답 DTO
 */
public class TokenResponse {

    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // --- Getter ---
    public String getAccessToken() {
        return accessToken;
    }

    // --- Setter ---
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}