package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class KakaoLoginRequest {
    @NotBlank
    private String authorizationCode;

    public KakaoLoginRequest() {
    }

    public KakaoLoginRequest(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}