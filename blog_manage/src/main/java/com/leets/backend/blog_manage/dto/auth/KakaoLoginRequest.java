package com.leets.backend.blog_manage.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * 카카오 로그인 요청 DTO
 */
public class KakaoLoginRequest {

    @NotBlank(message = "카카오 인증 코드가 필요합니다.")
    private String authorizationCode;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}