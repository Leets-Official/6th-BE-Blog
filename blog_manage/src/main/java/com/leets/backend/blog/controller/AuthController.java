package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.auth.*;
import com.leets.backend.blog.common.ApiResponse;
import com.leets.backend.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignUpRequest req) {
        authService.signup(req);

        return ResponseEntity.ok(ApiResponse.onSuccess("User registered"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest req) {
        TokenResponse tokens = authService.login(req);
        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "Login success", tokens));
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@RequestBody RefreshTokenRequest rreq) {
        TokenResponse tokens = authService.refreshToken(rreq.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "Token refreshed", tokens));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestParam String email) {
        authService.logout(email);
        return ResponseEntity.ok(ApiResponse.onSuccess("Logged out"));
    }
}
