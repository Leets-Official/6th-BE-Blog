package com.leets.backend.blog_manage.dto.auth;

public class KakaoSignupInfo {

    private String kakaoId;

    public KakaoSignupInfo(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public String getKakaoId() {
        return kakaoId;
    }
    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }
}