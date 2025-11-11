package com.leets.backend.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email; // 로그인 ID (email)

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String introduction;

    // 소셜 로그인 식별자
    @Column(nullable = true, unique = true)
    private String socialId;

    // 로그인 제공자 구분
    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private LocalDateTime birth;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 권한(ROLE_USER 등)
    @Column(nullable = false)
    private String role = "ROLE_USER";

    public User() {}

    // 회원가입 등에서 쓸 생성자
    public User(String email, String password, String name, String nickname, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.role = role != null ? role : "ROLE_USER";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getter
    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public String getIntroduction() { return introduction; }
    public String getSocialId() { return socialId; }
    public String getProvider() { return provider; }
    public LocalDateTime getBirth() { return birth; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getRole() { return role; }

    // Setter
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPassword(String password) { this.password = password; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public void setSocialId(String socialId) { this.socialId = socialId; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setBirth(LocalDateTime birth) { this.birth = birth; }
    public void setRole(String role) { this.role = role; }

    // 업데이트 편의 메서드
    public void update(String nickname, String password, String introduction) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
        if (introduction != null) {
            this.introduction = introduction;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public static User createDummy() {
        User user = new User();
        user.email = "dummy@naver.com";
        user.name = "김더미";
        user.nickname = "더미다";
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        return user;
    }
}
