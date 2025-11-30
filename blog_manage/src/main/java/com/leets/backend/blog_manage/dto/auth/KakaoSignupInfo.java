package com.leets.backend.blog_manage.dto.auth;

/**
 * 카카오 회원가입 정보 DTO
 * 회원가입 필요 시 카카오 ID 전달
 */
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