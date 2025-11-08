package com.leets.backend.blog_manage.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ... 나머지 필드들 ...
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

    private User(UserBuilder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.name = builder.name;
        this.profileImageUrl = builder.profileImageUrl;
        this.birthDate = builder.birthDate;
        this.introduction = builder.introduction;
        this.loginType = builder.loginType;
        this.kakaoId = builder.kakaoId;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String password;
        private String nickname;
        private String name;
        private String profileImageUrl;
        private LocalDate birthDate;
        private String introduction;
        private String loginType;
        private String kakaoId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder nickname(String nickname) { this.nickname = nickname; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder profileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }
        public UserBuilder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public UserBuilder introduction(String introduction) { this.introduction = introduction; return this; }
        public UserBuilder loginType(String loginType) { this.loginType = loginType; return this; }
        public UserBuilder kakaoId(String kakaoId) { this.kakaoId = kakaoId; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(this);
        }
    }

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