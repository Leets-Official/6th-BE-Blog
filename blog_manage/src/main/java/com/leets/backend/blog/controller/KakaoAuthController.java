package com.leets.backend.blog.controller;

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

    public KakaoAuthController(KakaoAuthService kakaoAuthService, RefreshTokenService tokenService) {
        this.kakaoAuthService = kakaoAuthService;
        this.tokenService = tokenService;
    }

    /**
     * ① 카카오 로그인 URL 제공
     * 프론트가 이 URL을 받아서 카카오 로그인 창으로 이동함
     */
    @GetMapping("/login-url")
    public ResponseEntity<Map<String, String>> getKakaoLoginUrl() {
        String loginUrl = kakaoAuthService.getKakaoLoginUrl();

        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", loginUrl);

        return ResponseEntity.ok(response);
    }

    /**
     * ② 카카오 redirect_uri 에서 받는 callback
     * 카카오가 인가코드(code)를 주면 백엔드가 이를 기반으로 로그인 수행
     */
    @GetMapping("/callback")
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestParam("code") String code) {
        KakaoLoginResponse response = kakaoAuthService.loginWithCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * ③ Refresh Token을 이용해 Access Token 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        String newAccessToken = tokenService.refreshAccessToken(refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }
}
