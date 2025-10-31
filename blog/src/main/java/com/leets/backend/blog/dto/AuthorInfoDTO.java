package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.User;

public class AuthorInfoDTO {
    private String nickname;
    private String introduction;

    public AuthorInfoDTO() { }

    public static AuthorInfoDTO from(User user) {
        AuthorInfoDTO dto = new AuthorInfoDTO();

        dto.nickname = user.getNickname();
        dto.introduction = user.getIntroduction();

        return dto;
    }

    // getters
    public String getNickname() { return nickname; }
    public String getIntroduction() { return introduction; }
}
