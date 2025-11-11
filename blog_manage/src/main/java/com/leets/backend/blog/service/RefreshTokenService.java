// src/main/java/com/leets/backend/blog/service/RefreshTokenService.java
package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String refreshAccessToken(String refreshTokenValue) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(refreshTokenValue);

        if (refreshTokenOpt.isEmpty()) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        RefreshToken refreshToken = refreshTokenOpt.get();
        String userEmail = refreshToken.getUser().getEmail();

        // 새로운 Access Token 생성
        return jwtTokenProvider.generateToken(userEmail);
    }
}
