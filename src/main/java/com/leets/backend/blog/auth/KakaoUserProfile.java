package com.leets.backend.blog.auth;

public class KakaoUserProfile {
    private final String kakaoId;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;
    private final String name;

    public KakaoUserProfile(String kakaoId,
                            String email,
                            String nickname,
                            String profileImageUrl,
                            String name) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }
}