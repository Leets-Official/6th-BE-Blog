package com.leets.backend.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequestDTO {
    private Boolean isKakaoLogin;
    private String kakaoId;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
