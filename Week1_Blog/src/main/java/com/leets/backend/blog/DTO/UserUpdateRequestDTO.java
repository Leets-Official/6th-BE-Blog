package com.leets.backend.blog.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserUpdateRequestDTO {
    private String profileImgUrl;
    @Email
    private String email;
    @Size(min = 8, max = 20)
    private String password;
    @Size(max = 10)
    private String name;
    private LocalDateTime birthDate;
    @Size(max = 20)
    private String nickname;
    @Size(max = 30)
    private String introduction;

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
