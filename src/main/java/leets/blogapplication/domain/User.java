package leets.blogapplication.domain;

import jakarta.persistence.*;
import leets.blogapplication.domain.enums.Provider;
import leets.blogapplication.domain.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User{
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

    //회원가입 방식 및 비밀번호
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=10)
    private Provider provider; // EMAIL or KAKAO
    @Column(nullable=false)
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
    @Column
    private String providerUserId; // KAKAO
    @Column
    private String passwordHash;   // EMAIL

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

    public static void createKakao(UserStatus status, String email, String name, LocalDate birthdate,
                String nickname, String intro, String profileImage, Provider provider,
                String providerUserId) {
        User user = new User();
        user.status = UserStatus.ACTIVE;
        user.email = email;
        user.name = name;
        user.birthdate = birthdate;
        user.nickname = nickname;
        user.intro = intro;
        user.profileImage = profileImage;
        user.provider = Provider.KAKAO;
        user.createdAt = LocalDateTime.now();
        user.providerUserId = providerUserId;
    }

    public static void createEmail(UserStatus status, String email, String name, LocalDate birthdate,
                                   String nickname, String intro, String profileImage,
                                   Provider provider, String passwordHash) {
        User user = new User();
        user.status = UserStatus.ACTIVE;
        user.email = email;
        user.name = name;
        user.birthdate = birthdate;
        user.nickname = nickname;
        user.intro = intro;
        user.profileImage = profileImage;
        user.provider = Provider.EMAIL;
        user.createdAt = LocalDateTime.now();
        user.passwordHash = passwordHash;
    }

    public void updateEmailUser(String nickname, String intro, String email, String passwordHash, String name,
                           LocalDate birthdate) {
        this.nickname = nickname;
        this.intro = intro;
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.passwordHash = passwordHash;
        this.updatedAt = LocalDateTime.now();
    }

//    public void updateKakaoUser() {}
    //카카오는 대체 뭘 변경한다는 건지 모르겟어서 일단 주석처리 했습니다

    protected User () {}
}
