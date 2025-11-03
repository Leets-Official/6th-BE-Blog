package com.leets.backend.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TokenResponseDTO {
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
        this.refreshToken = null;
    }

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
