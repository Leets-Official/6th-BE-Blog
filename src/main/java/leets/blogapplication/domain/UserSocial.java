package leets.blogapplication.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_social",
        uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_user_id"}))
public class UserSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String profile_nickname;

    @Column(name = "provider_user_id", nullable = false, length = 64)
    private Long providerUserId;

    @Column(nullable = false, length = 20)
    private String provider = "KAKAO";

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserSocial() {}

    public static UserSocial link(Long userId, String profile_nickname, Long providerUserId) {
        UserSocial us = new UserSocial();
        User u = new User();
        u.setId(userId); // 레퍼런스만 set
        u.setNickname(profile_nickname);
        us.setProviderUserId(providerUserId);
        return us;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProfile_nickname() { return profile_nickname; }
    public void setProfile_nickname(String profile_nickname) {}

    public Long getProviderUserId() { return providerUserId; }
    public void setProviderUserId(Long providerUserId) { this.providerUserId = providerUserId; }

    public User getUser() { return user;
    }

    public void setNickname(String nickname) { this.profile_nickname = nickname; }
}

