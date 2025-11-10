package com.leets.backend.blog.auth.controller.dto.response;

import com.leets.backend.blog.user.entity.User;

public class SignupResponse {
    private String email;
    private String nickname;

    public SignupResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static SignupResponse from(User user) {
        return new SignupResponse(user.getEmail(), user.getNickname());
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
