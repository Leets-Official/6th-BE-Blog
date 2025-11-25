package com.leets.backend.blog.auth.controller;

import com.leets.backend.blog.auth.controller.dto.request.SignupRequest;
import com.leets.backend.blog.auth.controller.dto.response.SignupResponse;
import com.leets.backend.blog.auth.controller.dto.response.TokenResponse;
import com.leets.backend.blog.auth.service.AuthService;
import com.leets.backend.blog.auth.service.KakaoOAuthService;
import com.leets.backend.blog.common.response.ApiResponse;
import com.leets.backend.blog.login.controller.dto.request.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final KakaoOAuthService kakaoOAuthService;

    public AuthController(AuthService authService,  KakaoOAuthService kakaoOAuthService) {
        this.authService = authService;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    // 이메일 회원가입 (프로필 사진 포함)
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request) {

        SignupResponse response = authService.signup(request, null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    // 이메일 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .body(ApiResponse.ok(tokenResponse));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissueToken(
            @CookieValue(name = "refreshToken") String refreshTokenClient) {

        TokenResponse tokenResponse = authService.reissue(refreshTokenClient);

        // 새 Refresh Token을 쿠키에 담아 반환 (Rotate Refresh Token)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 운영 환경에서는 true
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .body(ApiResponse.ok(tokenResponse));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        authService.logout(userId);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    // 카카오 OAuth 롤백
    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoCallback(@RequestParam String code) {
        TokenResponse tokenResponse = kakaoOAuthService.kakaoLogin(code);

        // JWT 토큰을 쿠키와 헤더에 설정하여 반환
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 운영 환경에서는 true
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .body(ApiResponse.ok(tokenResponse));
    }
}
