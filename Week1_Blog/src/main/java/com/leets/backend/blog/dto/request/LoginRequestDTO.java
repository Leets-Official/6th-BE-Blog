package com.leets.backend.blog.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public class LoginRequestDTO {
    @NotNull
    @JsonProperty("isKakaoLogin")
    private Boolean isKakaoLogin;
    private String kakaoId;
    @Email
    private String email;
    @Size(min = 8, max = 20)
    @NotBlank
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
