package com.leets.backend.blog_manage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 리프레시 토큰 엔티티
 */
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public RefreshToken() {}

    private RefreshToken(RefreshTokenBuilder builder) {
        this.id = builder.id;
        this.token = builder.token;
        this.expiryDate = builder.expiryDate;
        this.user = builder.user;
    }

    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }

    /** 리프레시 토큰 갱신 */
    public void updateToken(String token, LocalDateTime expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static class RefreshTokenBuilder {
        private Long id;
        private String token;
        private LocalDateTime expiryDate;
        private User user;

        public RefreshTokenBuilder id(Long id) { this.id = id; return this; }
        public RefreshTokenBuilder token(String token) { this.token = token; return this; }
        public RefreshTokenBuilder expiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }
        public RefreshTokenBuilder user(User user) { this.user = user; return this; }

        public RefreshToken build() {
            return new RefreshToken(this);
        }
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public User getUser() { return user; }
}