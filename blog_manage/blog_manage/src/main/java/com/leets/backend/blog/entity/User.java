package com.leets.backend.blog.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "NAME", length = 10)
    private String name; // O (Nullable)

    @Column(name = "EMAIL", length = 50, unique = true)
    private String email; // O (Nullable)

    @Column(name = "PASSWORD", length = 100)
    private String password; // O (Nullable)

    @Column(name = "NICKNAME", length = 20, nullable = false, unique = true)
    private String nickname; // NOT NULL

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate; // O (Nullable)

    @Column(name = "INTRO", length = 30)
    private String intro; // O (Nullable)

    @Column(name = "PROFILE_URL", length = 255)
    private String profileUrl; // O (Nullable)

    // ENUM 대신 간단한 String 타입으로 매핑 (DB ENUM 타입과 호환)
    @Column(name = "LOGIN_TYPE", length = 10, nullable = false)
    private String loginType; // NOT NULL (EMAIL/KAKAO 등 문자열 저장)

    @Column(name = "KAKAO_ID", length = 50, unique = true)
    private String kakaoId; // O (Nullable), UNIQUE

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt; // NOT NULL

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt; // O (Nullable)

    public User() {}

    // 회원가입 시 사용될 생성자
    public User(String email, String password, String nickname, String loginType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.createdAt = LocalDateTime.now(); // 생성 시점 자동 설정
        this.updatedAt = LocalDateTime.now(); // 업데이트 시점 자동 설정
    }

    // Getter (Lombok 금지)
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getIntro() { return intro; }
    public String getProfileUrl() { return profileUrl; }
    public String getLoginType() { return loginType; }
    public String getKakaoId() { return kakaoId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters (필요한 경우에만 추가)
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    // 사용자 정보 업데이트 메서드
    public void update(String nickname, String password, String profileUrl, LocalDateTime updatedAt) {
        if (nickname != null && !nickname.isEmpty()) {
            this.nickname = nickname;
        }
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
        this.updatedAt = updatedAt;
    }
}