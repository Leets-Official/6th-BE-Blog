package com.leets.backend.blog.dto.response;

import com.leets.backend.blog.entity.User;
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

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.isKakaoLogin = user.getIsKakaoLogin();
        dto.profileImgUrl = user.getProfileImgUrl();
        dto.email = user.getEmail();
        dto.password = user.getPassword();
        dto.name = user.getName();
        dto.birthDate = user.getBirthDate();
        dto.nickname = user.getNickname();
        dto.introduction = user.getIntroduction();

        return dto;
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
