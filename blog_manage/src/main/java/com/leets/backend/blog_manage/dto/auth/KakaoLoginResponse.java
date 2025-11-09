package com.leets.backend.blog_manage.dto.auth;

public class KakaoLoginResponse {
    private String status; // "LOGIN_SUCCESS" 또는 "SIGNUP_REQUIRED"
    private TokenResponse tokens; // LOGIN_SUCCESS 시
    private KakaoSignupInfo signupInfo; // SIGNUP_REQUIRED 시

    public KakaoLoginResponse(String status, TokenResponse tokens, KakaoSignupInfo signupInfo) {
        this.status = status;
        this.tokens = tokens;
        this.signupInfo = signupInfo;
    }

    // --- Getters ---
    public String getStatus() { return status; }
    public TokenResponse getTokens() { return tokens; }
    public KakaoSignupInfo getSignupInfo() { return signupInfo; }

    // --- Setters ---
    public void setStatus(String status) { this.status = status; }
    public void setTokens(TokenResponse tokens) { this.tokens = tokens; }
    public void setSignupInfo(KakaoSignupInfo signupInfo) { this.signupInfo = signupInfo; }
}