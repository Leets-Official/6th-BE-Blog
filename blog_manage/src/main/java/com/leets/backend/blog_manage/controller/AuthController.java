package com.leets.backend.blog_manage.controller;

import com.leets.backend.blog_manage.common.ApiResponse;
import com.leets.backend.blog_manage.dto.auth.*;
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

/**
 * 인증 컨트롤러
 * 회원가입, 로그인, 카카오 로그인 처리
 */
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
        return new ResponseEntity<>(
                ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", null),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "이메일 로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(request, response);
        return ResponseEntity.ok(
                ApiResponse.success("로그인 성공", tokenResponse)
        );
    }

    /** 카카오 로그인 - 기존 사용자는 로그인, 신규 사용자는 회원가입 필요 응답 */
    @Operation(summary = "카카오 로그인", description = "카카오 인증 코드로 로그인/회원가입 여부를 분기합니다.")
    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<KakaoLoginResponse>> kakaoLogin(
            @Valid @RequestBody KakaoLoginRequest request,
            HttpServletResponse response) {

        KakaoLoginResponse kakaoResponse = authService.kakaoLogin(request, response);

        String message = "SIGNUP_REQUIRED".equals(kakaoResponse.getStatus()) ?
                "카카오 인증 성공. 회원가입이 필요합니다." :
                "카카오 로그인 성공";

        return ResponseEntity.ok(
                ApiResponse.success(message, kakaoResponse)
        );
    }

    /** 카카오 회원가입 - 사용자 정보 입력 후 회원가입 완료 및 로그인 */
    @Operation(summary = "카카오 회원가입", description = "카카오 ID와 사용자가 직접 입력한 정보로 회원가입을 완료합니다.")
    @PostMapping("/kakao/signup")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoSignUp(
            @Valid @RequestBody KakaoSignUpRequest request,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.kakaoSignUp(request, response);

        return new ResponseEntity<>(
                ApiResponse.success("카카오 회원가입 및 로그인 성공", tokenResponse),
                HttpStatus.CREATED
        );
    }
}