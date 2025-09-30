package com.leets.backend.blog.domain;

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

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    @Column(length = 30, unique = true)
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

    // 기본 생성자
    public User() {}

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

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
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

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}