package com.leets.backend.blog_manage.dto.auth;

/**
 * 카카오 사용자 정보 응답 DTO
 * 카카오 고유 ID만 포함
 */
public class KakaoUserInfoResponse {

    private String id; // 카카오 고유 ID

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}