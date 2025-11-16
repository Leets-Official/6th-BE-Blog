package com.leets.backend.blog.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoResponseDTO {
    @JsonProperty("id")
    private Long id; // 카카오 고유 ID

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public KakaoUserInfoResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public void setKakaoAccount(KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }
}
