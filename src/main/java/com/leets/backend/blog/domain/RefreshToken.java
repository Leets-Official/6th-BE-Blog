package com.leets.backend.blog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RefreshToken {

    @Id
    private Long userId;

    @Column(nullable = false)
    private String token;

    // --- [수정/추가될 부분] ---

    /**
     * JPA를 위한 기본 생성자 (필수)
     */
    public RefreshToken() {}

    /**
     * [추가] AuthService의 'new RefreshToken(user.getId())' 호출을 위한 생성자
     * @param userId 토큰을 발급받는 사용자의 ID
     */
    public RefreshToken(Long userId) {
        this.userId = userId;
    }

    /**
     * (기존 코드) userId와 token을 모두 받는 생성자
     */
    public RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    /**
     * [추가] AuthService의 'rt.updateToken(refreshToken)' 호출을 위한 메소드
     * (기능은 setToken과 동일하지만, AuthService의 호출에 맞춰 이름을 제공합니다)
     * @param token 새로 발급된 리프레시 토큰 문자열
     */
    public void updateToken(String token) {
        this.token = token;
    }

    // --- (기존 Getter / Setter) ---

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}