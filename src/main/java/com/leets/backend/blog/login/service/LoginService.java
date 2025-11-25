package com.leets.backend.blog.login.service;

import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import com.leets.backend.blog.login.controller.dto.request.LoginRequest;
import com.leets.backend.blog.auth.controller.dto.response.TokenResponse;
import com.leets.backend.blog.common.config.jwt.JwtProvider;
import com.leets.backend.blog.user.entity.User;
import com.leets.backend.blog.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_USER_NOT_FOUND, "등록되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new  CustomException(ErrorCode.LOGIN_INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail(), user.getId());

        return new TokenResponse(accessToken, refreshToken, user.getNickname(), user.getId());
    }
}
