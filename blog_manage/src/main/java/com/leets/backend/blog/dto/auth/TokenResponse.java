package com.leets.backend.blog.dto.auth;

public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresIn; // ms 남은 시간 or 만료 epoch 등 필요에 따라 수정

    public TokenResponse() {}

    public TokenResponse(String accessToken, String refreshToken, long accessTokenExpiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    // getters / setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public long getAccessTokenExpiresIn() { return accessTokenExpiresIn; }
    public void setAccessTokenExpiresIn(long accessTokenExpiresIn) { this.accessTokenExpiresIn = accessTokenExpiresIn; }
}
