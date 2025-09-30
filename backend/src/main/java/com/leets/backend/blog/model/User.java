package com.leets.backend.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"nickname"}),
        @UniqueConstraint(columnNames = {"provider", "providerUserId"})
})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 1024)
    private String profileImage;

    @Column(length = 50, nullable = false)
    private String provider = "email"; //email 또는 kakao 가입

    @Column(length = 255)
    private String providerUserId;

    private LocalDateTime createdAt = LocalDateTime.now();

    // 기본 생성자
    public User() {}

    // 전체 생성자
    public User(Long userId, String email, String password, String nickname, String profileImage,
                 String provider, String providerUserId, String role, String status,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.createdAt = createdAt;
    }
}

