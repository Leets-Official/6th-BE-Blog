package com.leets.backend.blog.auth.service;

import com.leets.backend.blog.auth.client.KakaoApiClient;
import com.leets.backend.blog.auth.controller.dto.response.KakaoUserInfo;
import com.leets.backend.blog.auth.controller.dto.response.TokenResponse;
import com.leets.backend.blog.auth.entity.RefreshToken;
import com.leets.backend.blog.auth.repository.RefreshTokenRepository;
import com.leets.backend.blog.common.config.jwt.JwtProvider;
import com.leets.backend.blog.user.entity.User;
import com.leets.backend.blog.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KakaoOAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public KakaoOAuthService(KakaoApiClient kakaoApiClient,
                             UserRepository userRepository,
                             JwtProvider jwtProvider,
                             RefreshTokenRepository refreshTokenRepository) {
        this.kakaoApiClient = kakaoApiClient;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public TokenResponse kakaoLogin(String code) {
        String KakaoAccessToken = kakaoApiClient.getAccessToken(code);

        KakaoUserInfo kakaoUserInfo = kakaoApiClient.getUserInfo(KakaoAccessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() -> {
                    // 신규 회원: 소셜 로그인은 비밀번호가 없으므로 UUID로 더미 값 저장
                    User newUser = new User(
                            kakaoUserInfo.getEmail(),
                            UUID.randomUUID().toString(),
                            kakaoUserInfo.getNickname(),
                            kakaoUserInfo.getProfileImage()
                    );
                    return userRepository.save(newUser);
                });

        // JWT 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail(), user.getId());

        saveOrUpdateRefreshToken(user, refreshToken);

        return new TokenResponse(accessToken, refreshToken, user.getNickname(), user.getId());
    }
    // Refresh Token 저장/업데이트 헬퍼
    private void saveOrUpdateRefreshToken(User user, String refreshToken) {
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, jwtProvider.getRefreshTokenExpiry()),
                        () -> refreshTokenRepository.save(
                                new RefreshToken(user, refreshToken, jwtProvider.getRefreshTokenExpiry()))
                );
    }
}
