package com.leets.backend.blog.dto.response;

public class TokenResponseDTO {

    private String accessToken;
    private final String tokenType = "Bearer"; // 토큰 타입은 항상 Bearer로 고정

    public TokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
