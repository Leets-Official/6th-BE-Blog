package leets.blogapplication.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refId;

    @Column(nullable = false)
    private String token;

    @Column
    private LocalDateTime expiresAt;

    @Column
    private LocalDateTime issuedAt;

    @Column
    private boolean revoked = false;
    //true는 폐기, false는 사용 중

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public RefreshToken(String token, LocalDateTime expiresAt, LocalDateTime issuedAt, boolean revoked) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
        this.revoked = revoked;
    }

    protected RefreshToken() {}
}
