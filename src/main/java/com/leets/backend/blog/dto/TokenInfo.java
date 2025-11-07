package com.leets.backend.blog.dto;

public class TokenInfo {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    // @Builder
    public TokenInfo(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenInfo of(String grantType, String accessToken, String refreshToken) {
        return new TokenInfo(grantType, accessToken, refreshToken);
    }
    
    // --- Getters ---

    public String getGrantType() {
        return grantType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
