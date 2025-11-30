package leets.blogapplication.domain;

import jakarta.persistence.*;
import leets.blogapplication.domain.enums.Provider;
import leets.blogapplication.domain.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20)
    private UserStatus status;

    //UserProfile
    @Column(nullable=false, length=255)
    private String email;
    @Column(nullable=false, length=100)
    private String name;
    @Column(nullable=false)
    private LocalDate birthdate;
    @Column(nullable=false, length=50)
    private String nickname;
    @Column(nullable=false, length=200)
    private String intro;
    @Column(length=500)
    private String profileImage;

    @Column(nullable=false)
    private LocalDateTime createdAt;
    @Column(nullable=true)
    private LocalDateTime updatedAt;
    @Column
    private String password;   // EMAIL
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void addToken(RefreshToken ref) {
        refreshTokens.add(ref);
        ref.setUser(this);
    }
    public void removeToken(RefreshToken ref) {
        refreshTokens.remove(ref);
        ref.setUser(null);
        ref.setRevoked(false);
    }

    public static User newSocial(String email, String displayName) {
        User u = new User();
        u.setEmail(email);
        u.setPassword(null);
        u.setDisplayName(displayName != null ? displayName : "KakaoUser");
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }

    public static User createUserWithEmail(String email, String name, LocalDate birthdate,
                                   String nickname, String intro, String profileImage, String password) {
        User user = new User();
        user.status = UserStatus.ACTIVE;
        user.email = email;
        user.name = name;
        user.birthdate = birthdate;
        user.nickname = nickname;
        user.intro = intro;
        user.profileImage = profileImage;
        user.createdAt = LocalDateTime.now();
        user.password = password;
        return user;
    }

    public void updateUser(String nickname, String intro, String email, String password, String name,
                                LocalDate birthdate) {
        this.nickname = nickname;
        this.intro = intro;
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    protected User () {}

    // ====== 접근자 ======
    public Long getId() { return id; }
    public List<Post> getPosts() { return posts; }
    public List<Comment> getComments() { return comments; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return id.toString(); }

    public String getEmail() { return email; }

    // getters/setters
    public void setId(Long id) { this.id = id; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setNickname(String nickname) { this.nickname = nickname; }
}

