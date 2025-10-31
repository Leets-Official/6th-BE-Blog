package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileUrl;
    private final String loginType;
    private final LocalDateTime createdAt;
    private String token;

    public UserResponse(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileUrl = user.getProfileUrl();
        this.loginType = user.getLoginType();
        this.createdAt = user.getCreatedAt();
    }

    public UserResponse(User user, String token) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileUrl = user.getProfileUrl();
        this.loginType = user.getLoginType();
        this.createdAt = user.getCreatedAt();
        this.token = token;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getLoginType() {
        return loginType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getToken() {
        return token;
    }
}
