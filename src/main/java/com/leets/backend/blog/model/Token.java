package com.leets.backend.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="token")
public class Token {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="toke_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="token_hash", columnDefinition = "BINARY(32)", nullable = false)
    private byte[] tokenHash;

    @Column(name="issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name="expired_at", nullable = false)
    private LocalDateTime expiredAt;

    public Token(){}

    public Long getId(){return id;}
    public User getUser(){return user;}
    public byte[] getTokenHash(){return tokenHash;}
    public LocalDateTime getIssuedAt(){return issuedAt;}
    public LocalDateTime getExpiredAt(){return expiredAt;}
}
