package com.leets.backend.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SignUpRequestDTO {

    @NotBlank(message = "이메일은 비워둘 수 없습니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    private String password;

    @NotBlank(message = "이름은 비워둘 수 없습니다.")
    @Size(max = 10, message = "이름은 10자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "닉네임은 비워둘 수 없습니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하이어야 합니다.")
    private String nickname;

    private String profileImage;

    @Size(max = 30, message = "한 줄 소개는 최대 30글자입니다.")
    private String introduction;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public String getProfileImage() { return profileImage; }
    public String getIntroduction() { return introduction; }
    public LocalDate getBirthdate() { return birthdate; }
}
