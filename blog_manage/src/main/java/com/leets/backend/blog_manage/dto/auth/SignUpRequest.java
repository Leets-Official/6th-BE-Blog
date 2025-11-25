package com.leets.backend.blog_manage.dto.auth;

import com.leets.backend.blog_manage.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 이메일 회원가입 요청 DTO
 */
public class SignUpRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 적합하지 않습니다.")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 최대 10글자 입니다.")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 20, message = "닉네임은 최대 20글자 입니다.")
    private String nickname;

    private LocalDate birthDate;

    @Size(max = 30, message = "한 줄 소개는 최대 30글자 입니다.")
    private String introduction;

    // --- Getters ---
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPasswordConfirm() { return passwordConfirm; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getIntroduction() { return introduction; }

    // --- Setters (필요시) ---
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }
    public void setName(String name) { this.name = name; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }

    /** DTO를 Entity로 변환 (비밀번호 암호화) */
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password)) // 비밀번호 암호화
                .nickname(this.nickname)
                .name(this.name)
                .profileImageUrl("default_profile_image_url") // 기본값 설정 (피그마 참고)
                .birthDate(this.birthDate)
                .introduction(this.introduction)
                .loginType("EMAIL") // 이메일 가입
                .createdAt(LocalDateTime.now())
                .build();
    }
}