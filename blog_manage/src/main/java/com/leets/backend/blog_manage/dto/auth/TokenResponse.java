package com.leets.backend.blog_manage.dto.auth;

public class TokenResponse {

    private String accessToken;

    // 생성자
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