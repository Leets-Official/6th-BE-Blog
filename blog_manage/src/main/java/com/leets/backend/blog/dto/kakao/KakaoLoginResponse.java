package com.leets.backend.blog.dto.kakao;

public class KakaoLoginResponse {

    private String accessToken;
    private String refreshToken;
    private String email;
    private String nickname;

    public KakaoLoginResponse() {
    }

    public KakaoLoginResponse(String accessToken, String refreshToken, String email, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.nickname = nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
