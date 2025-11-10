package com.leets.backend.blog.entity;

import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens",
    uniqueConstraints = @UniqueConstraint(name="uk_refresh_user", columnNames="user_id"))
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 2048, unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Boolean revoked = false;

    private LocalDateTime expiresAt;


    // 기본 생성자
    public RefreshToken() {}

    // 전체 생성자
    public RefreshToken(Long tokenId, User user, String token, Boolean revoked,
                        LocalDateTime issuedAt, LocalDateTime expiresAt,
                        String userAgent, String ip) {
        this.tokenId = tokenId;
        this.user = user;
        this.token = token;
        this.revoked = revoked;
        this.expiresAt = expiresAt;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

}
