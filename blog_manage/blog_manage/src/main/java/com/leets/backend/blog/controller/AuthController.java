package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.dto.ApiResponse;
import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.UserLoginRequest;
import com.leets.backend.blog.dto.UserSignUpRequest;
import com.leets.backend.blog.dto.UserResponse;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody UserSignUpRequest request) {
        User newUser = authService.signUp(request);
        UserResponse userResponse = new UserResponse(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody UserLoginRequest request) {
        User user = authService.login(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwt = jwtTokenProvider.generateToken(authentication);
        UserResponse userResponse = new UserResponse(user, jwt);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}
