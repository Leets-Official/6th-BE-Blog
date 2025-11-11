package leets.blogapplication.domain;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long refreshTokenId;

    @Column(nullable = false)
    private String refreshToken;

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

    public void setUser(User user) { this.user = user; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }

    public RefreshToken(User user, String token, LocalDateTime expiresAt, LocalDateTime issuedAt, boolean revoked) {
        this.refreshToken = token;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
        this.revoked = revoked;
        this.user = user;
    }

    public static RefreshToken createRefreshToken(String token, User user){
        RefreshToken ref = new RefreshToken(user, token, LocalDateTime.now().plusDays(7), LocalDateTime.now(), false);
        return ref;
    }

    protected RefreshToken() {}

    public Long getUserId() { return user.getId();
    }
}
