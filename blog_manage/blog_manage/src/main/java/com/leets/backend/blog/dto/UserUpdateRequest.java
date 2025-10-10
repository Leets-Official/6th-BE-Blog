package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하이어야 합니다.")
    private String nickname;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
    private String password;

    private String profileUrl; // 프로필 사진 URL은 필수가 아님

    // Getters
    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
