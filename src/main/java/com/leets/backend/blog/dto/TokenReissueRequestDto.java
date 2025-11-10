package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenReissueRequestDto {

    @NotBlank(message = "Refresh Token은 필수입니다.")
    private String refreshToken;

    // --- Getter and Setter ---

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}