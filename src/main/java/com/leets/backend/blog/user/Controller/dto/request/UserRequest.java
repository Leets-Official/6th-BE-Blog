package com.leets.backend.blog.user.controller.dto.request;

import jakarta.validation.constraints.*;

public class UserRequest {
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해야 합니다.")
    private String nickname;

    public UserRequest() {}

    public UserRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
