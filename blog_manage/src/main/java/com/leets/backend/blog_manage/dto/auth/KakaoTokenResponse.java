package com.leets.backend.blog_manage.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

// 카카오 토큰 응답 DTO
public class KakaoTokenResponse {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;

    // --- Getters ---
    public String getTokenType() { return tokenType; }
    public String getAccessToken() { return accessToken; }
    public Integer getExpiresIn() { return expiresIn; }
    public String getRefreshToken() { return refreshToken; }
    public Integer getRefreshTokenExpiresIn() { return refreshTokenExpiresIn; }
    public String getScope() { return scope; }

    // --- Setters ---
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setExpiresIn(Integer expiresIn) { this.expiresIn = expiresIn; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setRefreshTokenExpiresIn(Integer refreshTokenExpiresIn) { this.refreshTokenExpiresIn = refreshTokenExpiresIn; }
    public void setScope(String scope) { this.scope = scope; }
}