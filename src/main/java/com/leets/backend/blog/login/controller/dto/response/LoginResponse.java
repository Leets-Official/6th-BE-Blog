package com.leets.backend.blog.login.controller.dto.response;

public class LoginResponse {
    private boolean success = true;
    private String nickname;

    public LoginResponse() {}

    public LoginResponse(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getNickname() {
        return nickname;
    }
}
