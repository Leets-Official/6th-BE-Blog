package com.leets.backend.blog.domain;

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

    // Getter (Lombok 금지)
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getIntroduction() { return intro; }
    public String getProfileUrl() { return profileUrl; }
    public String getLoginType() { return loginType; }
    public String getKakaoId() { return kakaoId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

}
