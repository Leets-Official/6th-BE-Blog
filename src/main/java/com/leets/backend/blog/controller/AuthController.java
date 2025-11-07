package com.leets.backend.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.backend.blog.dto.TokenInfo;
import com.leets.backend.blog.dto.TokenReissueRequestDto;
import com.leets.backend.blog.dto.UserSignUpRequestDto;
import com.leets.backend.blog.dto.UserLoginRequestDto;
import com.leets.backend.blog.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Auth API", description = "사용자 인증(회원가입, 로그인, 토큰) 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원가입 API
     * POST /auth
     */
    @Operation(summary = "회원가입", description = "이메일, 닉네임, 비밀번호로 회원가입을 진행합니다.")
    @PostMapping
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * 로그인 API
     * POST /auth/login
     */
    @Operation(summary = "로그인", description = "이메일, 비밀번호로 로그인을 진행하고 토큰(Access, Refresh)을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        // !! try-catch 제거 !!
        // 서비스에서 예외 발생 시 GlobalExceptionHandler가 처리합니다.
        TokenInfo tokenInfo = authService.login(requestDto);
        return ResponseEntity.ok(tokenInfo);
    }

    /**
     * 토큰 재발급 API
     * POST /auth/reissue
     */
    @Operation(summary = "토큰 재발급", description = "유효한 Refresh Token을 사용하여 Access Token과 Refresh Token을 재발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<TokenInfo> reissueToken(@Valid @RequestBody TokenReissueRequestDto requestDto) {
        TokenInfo tokenInfo = authService.reissueToken(requestDto.getRefreshToken());
        return ResponseEntity.ok(tokenInfo);
    }
}