package com.leets.backend.blog.user.controller.dto.request;

import jakarta.validation.constraints.*;

public class UserPasswordUpdateRequest {
    @NotBlank(message = "현재 비밀번호는 필수 입력 항목입니다.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String newPassword;

    public UserPasswordUpdateRequest() {}

    public UserPasswordUpdateRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
