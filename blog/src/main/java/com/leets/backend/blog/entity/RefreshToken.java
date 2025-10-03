package com.leets.backend.blog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expirationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Getters
    public Long getTokenId() {
        return tokenId;
    }
    public String getToken() {
        return token;
    }
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    public User getUser() {
        return user;
    }
}
