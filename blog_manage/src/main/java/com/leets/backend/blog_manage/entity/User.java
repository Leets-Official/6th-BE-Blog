package com.leets.backend.blog_manage.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user") // 테이블 이름을 명시적으로 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false, unique = true)
    private String nickname;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    private LocalDate birthDate;

    @Column(length = 30)
    private String introduction;

    @Column(length = 10, nullable = false)
    private String loginType;

    @Column(length = 100)
    private String kakaoId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public User() {}

    // --- Getters ---
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getName() { return name; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getIntroduction() { return introduction; }
    public String getLoginType() { return loginType; }
    public String getKakaoId() { return kakaoId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}