package com.leets.backend.blog.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {
    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    public Profile() {}

    // Getters and Setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
