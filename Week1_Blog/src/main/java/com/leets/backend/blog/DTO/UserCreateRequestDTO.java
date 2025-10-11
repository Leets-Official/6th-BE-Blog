package com.leets.backend.blog.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Bag;

import java.time.LocalDateTime;

public class UserCreateRequestDTO {
    private Boolean isKakaoLogin;
    private String kakaoId;
    private String profileImgUrl;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    @Size(max = 10)
    private String name;
    private LocalDateTime birthDate;
    @NotBlank
    @Size(max = 20)
    private String nickname;
    @Size(max = 30)
    private String introduction;

    public Boolean getIsKakaoLogin() {
        return isKakaoLogin;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIntroduction() {
        return introduction;
    }
}
