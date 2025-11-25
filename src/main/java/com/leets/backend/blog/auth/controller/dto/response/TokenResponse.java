package com.leets.backend.blog.auth.controller.dto.response;

public class TokenResponse {
    private final String accessToken;
    private final String refreshToken;
    private final String nickname;
    private final Long userId;

    public TokenResponse(String accessToken, String refreshToken, String nickname, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() { return refreshToken; }
    public String getNickname() { return nickname; }
    public Long getUserId() { return userId; }
}
