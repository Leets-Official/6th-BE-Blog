package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.AuthResponse;
import com.leets.backend.blog.dto.LoginRequest;
import com.leets.backend.blog.dto.SignupRequest;
import com.leets.backend.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 서비스", description = "로그인/회원가입 CRUD API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @Operation(summary = "회원가입", description = "사용자는 이메일, 비밀번호, 닉네임 기입시 회원가입이 가능합니다. (단, 중복된 정보로는 불가능)", security = {})
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest req){
        authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인", description = "사용자는 가입시 기입한 회원 정보로 로그인이 가능합니다.", security = {})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req,
                                              HttpServletResponse res){
        AuthResponse body = authService.login(req, res);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "refresh 쿠키", description = "새로운 access 토큰을 발급합니다.", security = {})
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest req, HttpServletResponse res){
        return ResponseEntity.ok(authService.refresh(req, res));
    }

    @Operation(summary = "로그아웃", description = "쿠키가 삭제되고 refresh 토큰이 회수됩니다.", security = {})
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        authService.logout(req, res);
        return ResponseEntity.ok("성공적으로 로그아웃 되었습니다.");
    }
}
