package com.leets.backend.blog.DTO;

import com.leets.backend.blog.domain.User;
import java.time.LocalDateTime;

public class UserResponseDTO {
    private Boolean isKakaoLogin;
    private String profileImgUrl;
    private String email;
    private String password;
    private String name;
    private LocalDateTime birthDate;
    private String nickname;
    private String introduction;

    public UserResponseDTO(User user) {
        this.isKakaoLogin = user.getIsKakaoLogin();
        this.profileImgUrl = user.getProfileImgUrl();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.birthDate = user.getBirthDate();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
    }

    public Boolean getKakaoLogin() {
        return isKakaoLogin;
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
