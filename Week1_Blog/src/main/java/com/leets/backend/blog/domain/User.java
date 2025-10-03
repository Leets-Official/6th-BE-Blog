package com.leets.backend.blog.domain;

import com.leets.backend.blog.util.StringUtil;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

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

    @OneToMany(mappedBy = "userId")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<Comment> comments = new ArrayList<>();

    // 기본 생성자
    protected User() {}

    // 이메일 회원 가입용
    public static User createByEmail(String name, String email, String password, String nickname, LocalDateTime birthDate, String introduction, String profileImgUrl) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.isKakaoLogin = false;
        user.nickname = nickname;
        user.birthDate = birthDate;
        user.introduction = introduction;
        user.profileImgUrl = profileImgUrl;
        return user;
    }

    // 카카오 로그인용
    public static User createByKakao(String kakaoId, String name, String email, String password, String nickname, LocalDateTime birthDate, String introduction, String profileImgUrl) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.isKakaoLogin = true;
        user.kakaoId =  kakaoId;
        user.nickname = nickname;
        user.birthDate = birthDate;
        user.introduction = introduction;
        user.profileImgUrl = profileImgUrl;
        return user;
    }

    public void updateUser(String nickname, String email, String introduction, String name, String birthDate){
        this.nickname = StringUtil.isNullOrEmpty(nickname) ? this.nickname : nickname;
        this.email = StringUtil.isNullOrEmpty(email) ? this.email : email;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public List<Post> getMyPosts() {
        return posts;
    }
}
