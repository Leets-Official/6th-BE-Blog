package com.leets.backend.blog.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column
    private LocalDate birthday;

    @Column(length = 30)
    private String description;

    @Column(name = "login_method", nullable = false)
    private String loginMethod; // 추후 Enum 클래스로 리팩토링 예정

    @Column(name = "kakao_id", length = 100)
    private String kakaoId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected User() {}

    // Getters
    public Long getUserId() { return userId; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public LocalDate getBirthday() { return birthday; }
    public String getDescription() { return description; }
    public String getLoginMethod() { return loginMethod; }
    public String getKakaoId() { return kakaoId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
