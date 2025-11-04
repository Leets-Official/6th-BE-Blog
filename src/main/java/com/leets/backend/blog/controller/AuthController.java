package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.TokenInfo; // 1. import 추가
import com.leets.backend.blog.dto.TokenReissueRequestDto; // 2. import 추가
import org.springframework.http.HttpStatus; // 3. import 추가
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.backend.blog.dto.UserSignUpRequestDto;
import com.leets.backend.blog.service.AuthService;

import jakarta.validation.Valid;

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
    @PostMapping
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        // 4. 예외 처리를 위해 try-catch 추가
        try {
            authService.signUp(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            // 이메일/닉네임 중복 시 400 Bad Request 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 5. 토큰 재발급 API 구현 (주석 해제 및 수정)
     * POST /auth/reissue
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@Valid @RequestBody TokenReissueRequestDto requestDto) {
        try {
            TokenInfo tokenInfo = authService.reissueToken(requestDto);
            return ResponseEntity.ok(tokenInfo);
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 토큰(만료, 위조, DB에 없음 등)일 경우 401 Unauthorized 반환
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) 
                    .body(e.getMessage());
        }
    }
}

