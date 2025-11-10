package com.leets.backend.blog.login.controller;

import com.leets.backend.blog.common.response.ApiResponse;
import com.leets.backend.blog.login.controller.dto.request.LoginRequest;
import com.leets.backend.blog.login.controller.dto.response.TokenResponse;
import com.leets.backend.blog.login.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 이메일 로그인
    @PostMapping
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = loginService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(response));
    }
}
