package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.User;

public class LoginUserInfoDTO {

    private String nickname;

    public LoginUserInfoDTO() { }

    public static LoginUserInfoDTO from(User user) {
        LoginUserInfoDTO dto = new LoginUserInfoDTO();
        dto.nickname = user.getNickname();
        return dto;
    }

    // getters
    public String getNickname() { return nickname; }
}
