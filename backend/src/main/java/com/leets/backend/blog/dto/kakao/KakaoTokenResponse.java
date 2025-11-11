package com.leets.backend.blog.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    // 필요시 더 추가 예정

    public String getAccessToken() {
        return accessToken;
    }
}
