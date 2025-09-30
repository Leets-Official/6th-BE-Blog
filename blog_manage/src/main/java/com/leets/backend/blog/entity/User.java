package com.leets.backend.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "kakao_id", nullable = true)
    private String kakaoId;

    // Getters
    public Long getUserId() {
        return userId;
    }
    public String getNickname() {
        return nickname;
    }
    public String getIntroduction() {
        return introduction;
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
    public LocalDate getBirth() {
        return birth;
    }
    public String getKakaoId() {
        return kakaoId;
    }
}
