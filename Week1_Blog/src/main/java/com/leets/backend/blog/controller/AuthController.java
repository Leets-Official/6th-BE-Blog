package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.request.LoginRequestDTO;
import com.leets.backend.blog.dto.request.UserCreateRequestDTO;
import com.leets.backend.blog.dto.response.LoginResponseDTO;
import com.leets.backend.blog.dto.response.UserResponseDTO;
import com.leets.backend.blog.security.JwtTokenProvider;
import com.leets.backend.blog.service.AuthService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원 인증 및 토큰 refresh 요청")
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService service;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthService service, JwtTokenProvider tokenProvider) {
        this.service = service;
        this.tokenProvider = tokenProvider;
    }

    @Operation(
            summary = "로그인 인증",
            description = "로그인 요청, 성공 시 success: true, 닉네임, access token 반환, refresh token은 HttpOnly 쿠키로 설정"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = service.userLogin(request);

        String refreshToken = tokenProvider.createRefreshToken(request.getEmail());
        String accessToken = tokenProvider.createAccessToken(request.getEmail());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // HTTPS 환경에서만 true
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 (원하는 만료시간으로 조정)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(response);
    }

    @Operation(
            summary = "토큰 리프레쉬",
            description = "쿠키의 refresh토큰으로 클라이언트에 새로운 access토큰 발급"
    )
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(value = "refreshToken", required = false) String refreshTokenFromCookie) {
        if (StringUtils.isEmpty(refreshTokenFromCookie))
            return ResponseEntity.status(401).build();

        String newAccessToken = service.refreshAccessToken(refreshTokenFromCookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .build();
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃 + refresh 토큰 삭제"
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshTokenFromCookie) {
        if (!StringUtils.isEmpty(refreshTokenFromCookie)) { // db에 있는 refresh토큰 삭제
            service.logout(refreshTokenFromCookie);
        }
        // refreshToken 쿠키 삭제 (만료)
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")   // 프론트 백 다른 도메인이어도 처리
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", deleteCookie.toString())
                .body("로그아웃 완료");
    }

    @Operation(
            summary = "신규 회원 생성",
            description = "회원가입을 성공적으로 마친 회원의 정보에 따라 회원 새로 생성"
    )
    @PostMapping("/register")
    public UserResponseDTO createUser(@Valid @RequestBody UserCreateRequestDTO dto){
        return service.createUser(dto);
    }
}
