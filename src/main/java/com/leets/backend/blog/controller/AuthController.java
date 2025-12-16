package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.LoginRequest;
import com.leets.backend.blog.dto.LoginResponse;
import com.leets.backend.blog.dto.SignupRequest;
import com.leets.backend.blog.service.AuthService;
import com.leets.backend.blog.dto.KakaoLoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest req) {
        authService.register(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> loginWithKakao(@Valid @RequestBody KakaoLoginRequest req) {
        return ResponseEntity.ok(authService.loginWithKakao(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String refreshTokenPlain) {
        return ResponseEntity.ok(authService.refreshAccess(refreshTokenPlain));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String refreshTokenPlain) {
        authService.logout(refreshTokenPlain);
        return ResponseEntity.ok().build();
    }
}
