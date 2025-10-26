package com.leets.backend.blog.entity;

import com.leets.backend.blog.enums.LoginMethod;
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
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(nullable = false, length = 10)
    private String name;
    private LocalDate birthdate;
    @Column(nullable = false, length = 20)
    private String nickname;
    @Column(length = 30)
    private String introduction;
    private String profileImage;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;
    @Column(unique = true)
    private String kakaoId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User() {}

    public User createDummy() {
        User user = new User();
        user.email = "dummy@naver.com";
        user.name = "김더미";
        user.nickname = "더미다";
        user.loginMethod = LoginMethod.KAKAO;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();

        return user;
    }

    //Getters
    public Long getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public String getNickname() {
        return nickname;
    }
    public String getIntroduction() {
        return introduction;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public LoginMethod getLoginMethod() {
        return loginMethod;
    }
    public String getKakaoId() {
        return kakaoId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}