package com.leets.backend.blog_manage.controller;

import com.leets.backend.blog_manage.common.ApiResponse;
import com.leets.backend.blog_manage.dto.auth.LoginRequest;
import com.leets.backend.blog_manage.dto.auth.SignUpRequest;
import com.leets.backend.blog_manage.dto.auth.TokenResponse;
import com.leets.backend.blog_manage.entity.User;
import com.leets.backend.blog_manage.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 (Auth) API", description = "사용자 회원가입 및 로그인 관련 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "이메일 회원가입", description = "이메일과 비밀번호, 닉네임 등으로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signUp(@Valid @RequestBody SignUpRequest request) {
        User savedUser = authService.signUp(request);

        // 회원가입 성공 시, 응답 본문에 별도 데이터 없이 성공 메시지만 반환
        return new ResponseEntity<>(
                ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", null),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "이메일 로그인", description = "이메일과 비밀번호로 로그인합니다. 성공 시 AccessToken을 반환하고 RefreshToken을 쿠키에 설정합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) { // RefreshToken을 쿠키로 받기 위해 Response 객체 필요

        TokenResponse tokenResponse = authService.login(request, response);

        return ResponseEntity.ok(
                ApiResponse.success("로그인 성공", tokenResponse)
        );
    }
}