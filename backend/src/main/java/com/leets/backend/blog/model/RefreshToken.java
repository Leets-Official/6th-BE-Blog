package com.leets.backend.blog.model;

import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 2048, unique = true, nullable = false)
    private String token;

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
}
