package com.leets.backend.blog.dto.response;

public class LoginResponseDTO {
    private boolean success = true;
    private String nickname;
    private String email;  // 이메일 혹은 kakaoId

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}
