package com.leets.backend.blog.dto.response;

public class LoginResponseDTO {
    private boolean success = true;
    private String nickname;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getNickname() {
        return nickname;
    }
}
