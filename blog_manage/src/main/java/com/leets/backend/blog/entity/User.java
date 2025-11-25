package com.leets.backend.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String introduction;

    @Column(nullable = true)
    private String kakaoId;

    @Column(nullable = true)
    private LocalDateTime birth;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User() {}


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
    public String getKakaoId() { return kakaoId; }
    public LocalDateTime getBirth() { return birth; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setter / 업데이트 관련 메서드
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


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
