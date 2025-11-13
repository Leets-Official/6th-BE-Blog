package leets.blogapplication.domain;

import jakarta.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_social_id", nullable = false)
    private UserSocial userSocial;

    public void setUser(User user) { this.user = user; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }

    public RefreshToken(User user, String token, LocalDateTime expiresAt, LocalDateTime issuedAt, boolean revoked) {
        this.refreshToken = token;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
        this.revoked = revoked;
        this.user = user;
    }

    public RefreshToken(UserSocial userSocial, String token, LocalDateTime expiresAt, LocalDateTime issuedAt, boolean revoked) {
        this.refreshToken = token;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
        this.revoked = revoked;
        this.userSocial = userSocial;
    }

    public static RefreshToken createRefreshToken(String token, User user){
        RefreshToken ref = new RefreshToken(user, token, LocalDateTime.now().plusDays(7), LocalDateTime.now(), false);
        return ref;
    }

    public static RefreshToken createRefreshToken(String token, UserSocial userSocial){
        RefreshToken ref = new RefreshToken(userSocial, token, LocalDateTime.now(), LocalDateTime.now(), false);
        return ref;
    }

    protected RefreshToken() {}

    public Long getUserId() { return user.getId(); }
    public Long getSocialUserId() { return userSocial.getId(); }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
