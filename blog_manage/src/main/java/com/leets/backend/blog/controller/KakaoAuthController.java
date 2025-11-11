package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.kakao.KakaoLoginRequest;
import com.leets.backend.blog.dto.kakao.KakaoLoginResponse;
import com.leets.backend.blog.service.KakaoAuthService;
import com.leets.backend.blog.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final RefreshTokenService tokenService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService, RefreshTokenService refreshTokenService) {
        this.kakaoAuthService = kakaoAuthService;
        this.tokenService = refreshTokenService;
    }

    // 카카오 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        KakaoLoginResponse response = kakaoAuthService.kakaoLogin(request.getAccessToken());
        return ResponseEntity.ok(response);
    }

    // Refresh Token으로 새로운 Access Token 발급
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }
}
