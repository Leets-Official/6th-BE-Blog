package com.leets.backend.blog.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoAccount {

    @JsonProperty("email")
    private String email;

    @JsonProperty("profile")
    private Profile profile;

    public KakaoAccount() {}

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
