package com.leets.backend.blog_manage.dto.auth;

import com.leets.backend.blog_manage.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class KakaoSignUpRequest {

    // (백엔드에서 전달받아 프론트가 다시 보내주는) 카카오 고유 ID
    @NotBlank
    private String kakaoId;

    // --- 사용자가 폼에서 직접 입력하는 정보 ---
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 적합하지 않습니다.")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 최대 10글자 입니다.")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 20, message = "닉네임은 최대 20글자 입니다.")
    private String nickname;

    private LocalDate birthDate;

    @Size(max = 30, message = "한 줄 소개는 최대 30글자 입니다.")
    private String introduction;

    // (참고: 프로필 사진은 피그마상 입력 폼에 없으므로 기본값 처리)

    // DTO를 Entity로 변환
    public User toEntity(PasswordEncoder passwordEncoder) {
        // 카카오 로그인 사용자는 비밀번호를 사용하지 않으므로,
        // NOT NULL 제약조건(ERD)을 맞추기 위해 임의의 값을 암호화하여 저장
        String dummyPassword = "KAKAO_USER_DUMMY_PASSWORD_" + UUID.randomUUID();

        return User.builder()
                .email(this.email) // 사용자가 입력한 이메일
                .password(passwordEncoder.encode(dummyPassword)) // 임시 비밀번호 암호화
                .nickname(this.nickname) // 사용자가 입력한 닉네임
                .name(this.name) // 사용자가 입력한 이름
                .profileImageUrl("default_profile_image_url") // 기본값 설정
                .birthDate(this.birthDate) // 사용자가 입력한 생년월일
                .introduction(this.introduction) // 사용자가 입력한 한줄소개
                .loginType("KAKAO") // 로그인 타입
                .kakaoId(this.kakaoId) // 카카오 ID
                .createdAt(LocalDateTime.now())
                .build();
    }

    // --- Getters ---
    public String getKakaoId() { return kakaoId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getIntroduction() { return introduction; }

    // --- Setters ---
    public void setKakaoId(String kakaoId) { this.kakaoId = kakaoId; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
}