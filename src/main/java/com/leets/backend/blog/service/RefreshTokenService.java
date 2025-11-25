package com.leets.backend.blog.service;

import com.leets.backend.blog.domain.RefreshToken;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = new RefreshToken(userId, newRefreshToken);
        refreshTokenRepository.save(refreshToken);
    }
}