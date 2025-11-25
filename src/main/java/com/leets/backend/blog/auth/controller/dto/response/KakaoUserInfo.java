package com.leets.backend.blog.auth.controller.dto.response;

public class KakaoUserInfo {
    private final String email;
    private final String nickname;
    private final String profileImage;

    public KakaoUserInfo(String email, String nickname, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
