package com.leets.backend.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank(message = "이메일은 비워둘 수 없습니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    private String password;

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
