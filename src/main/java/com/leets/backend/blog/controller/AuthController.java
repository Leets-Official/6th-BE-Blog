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
// [추가] 로그인 DTO를 import 합니다.
import com.leets.backend.blog.dto.UserLoginRequestDto;
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
     * [추가된 로그인 API]
     * 로그인 API
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        try {
            // AuthService에 login(UserLoginRequestDto) 메소드가 구현되어 있어야 합니다.
            // 이 메소드는 인증 성공 시 TokenInfo를 반환합니다.
            TokenInfo tokenInfo = authService.login(requestDto);

            // 로그인 성공 시 200 OK와 함께 토큰 정보(TokenInfo)를 응답 본문에 반환
            return ResponseEntity.ok(tokenInfo);

        } catch (IllegalArgumentException e) { // 또는 Spring Security의 AuthenticationException
            // authService.login에서 이메일/비밀번호 불일치 시
            // 예외를 발생시킨다고 가정합니다.
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401: 인증 실패
                    .body(e.getMessage()); // "이메일 또는 비밀번호가 올바르지 않습니다."
        }
    }


    /**
     * 5. 토큰 재발급 API 구현 (기존 코드)
     * POST /auth/reissue
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@Valid @RequestBody TokenReissueRequestDto requestDto) {
        try {
            // [수정된 부분]
            // requestDto 객체 대신, 그 안의 Refresh Token 문자열을 전달합니다.
            TokenInfo tokenInfo = authService.reissueToken(requestDto.getRefreshToken());

            return ResponseEntity.ok(tokenInfo);
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 토큰(만료, 위조, DB에 없음 등)일 경우 401 Unauthorized 반환
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}