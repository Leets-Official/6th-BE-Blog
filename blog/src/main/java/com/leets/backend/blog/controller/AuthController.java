package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.ApiResponse;
import com.leets.backend.blog.config.JwtProperties;
import com.leets.backend.blog.dto.LoginRequestDTO;
import com.leets.backend.blog.dto.SignUpRequestDTO;
import com.leets.backend.blog.dto.TokenResponseDTO;
import com.leets.backend.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "사용자 인증 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    public AuthController(AuthService authService, JwtProperties jwtProperties) {
        this.authService = authService;
        this.jwtProperties = jwtProperties;
    }

    // 이메일 회원가입
    @Operation(summary = "이메일 회원가입", description = "이메일 주소로 회원가입을 합니다.")
    @PostMapping("/signup/email")
    public ResponseEntity<ApiResponse<String>> emailSignUp(
            @Valid @RequestBody SignUpRequestDTO request
    ) throws AuthException {

        authService.signUp(request);

        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.CREATED, "이메일 회원가입 성공"),
                HttpStatus.CREATED
        );
    }

    // 이메일 로그인
    @Operation(summary = "이메일 로그인", description = "이메일 주소로 로그인을 합니다.")
    @PostMapping("/login/email")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> emailLogin(
            @Valid @RequestBody LoginRequestDTO request
    ) {

        TokenResponseDTO tokens = authService.login(request);

        // Refresh Token으로 HttpOnly 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true) // HTTPS
                .sameSite("Strict") // CSRF 방어
                .path("/auth") // 쿠키 사용 경로 제한
                .maxAge(jwtProperties.getRefreshTokenExpirationMs() / 1000) // 만료 시간(초)
                .build();

        // 응답 본문에는 Access Token만 포함
        TokenResponseDTO responseBody = new TokenResponseDTO(tokens.getAccessToken());

        // 응답 헤더에 쿠키 추가, 본문에 Access Token 추가
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.onSuccess(HttpStatus.OK, "이메일 로그인 성공", responseBody));
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "Refresh Token을 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) throws AuthException {

        // 쿠키에서 읽은 토큰으로 로그아웃 처리
        authService.logout(refreshToken);

        // 클라이언트의 쿠키를 삭제하기 위한 만료 쿠키 직접 생성 (CookieUtil 대체)
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(0) // 즉시 만료
                .build();

        // 헤더에 만료 쿠키를 설정하여 응답
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(ApiResponse.onSuccess(HttpStatus.OK, "로그아웃 성공"));
    }

    // 토큰 재발급
    @Operation(summary = "Access Token 재발급", description = "Access Token을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = true) String refreshToken
    ) throws AuthException {

        TokenResponseDTO responseBody = authService.refresh(refreshToken);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(HttpStatus.OK, "액세스 토큰 재발급 성공", responseBody)
        );
    }
}
