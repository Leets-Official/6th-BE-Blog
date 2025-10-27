package com.leets.backend.blog.DTO;

import jakarta.validation.constraints.*;

public class LoginRequestDTO {
    @NotBlank
    private Boolean isKakaoLogin;
    @NotBlank
    private String kakaoId;
    @Email
    private String email;
    @Size(min = 8, max = 20)
    private String password;


    public Boolean getKakaoLogin() {
        return isKakaoLogin;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
