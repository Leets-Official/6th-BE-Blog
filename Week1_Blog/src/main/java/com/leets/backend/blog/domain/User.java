package com.leets.backend.blog.domain;

import com.leets.backend.blog.DTO.UserCreateRequestDTO;
import com.leets.backend.blog.util.StringUtil;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // DB 테이블명
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_kakao_login", nullable = false)
    private Boolean isKakaoLogin;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    @Column(length = 20, unique = true)
    private String nickname;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    private String introduction;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    // 기본 생성자
    protected User() {}

    // 카카오 & 이메일 회원가입
    public static User createUser(UserCreateRequestDTO dto){
        User user = new User();
        user.name = dto.getName();
        user.email = dto.getEmail();
        user.password = dto.getPassword();
        user.isKakaoLogin = dto.getIsKakaoLogin();
        user.kakaoId = dto.getIsKakaoLogin() ? dto.getKakaoId() : null;
        user.nickname = dto.getNickname();
        user.birthDate = dto.getBirthDate();
        user.introduction = dto.getIntroduction();
        user.profileImgUrl = dto.getProfileImgUrl();
        user.createDate = LocalDateTime.now();
        return user;
    }

    public void updateUser(String nickname, String email, String password, String introduction, String name, String birthDate){
        this.nickname = StringUtil.isNullOrEmpty(nickname) ? this.nickname : nickname;
        this.email = StringUtil.isNullOrEmpty(email) ? this.email : email;
        this.password = StringUtil.isNullOrEmpty(password) ? this.password : password;
        this.name = StringUtil.isNullOrEmpty(name) ? this.name : name;
        this.birthDate = StringUtil.isNullOrEmpty(birthDate) ? this.birthDate : LocalDateTime.parse(birthDate);;
        this.updateDate = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getIsKakaoLogin() {
        return  isKakaoLogin;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public List<Post> getMyPosts() {
        return posts;
    }
}
