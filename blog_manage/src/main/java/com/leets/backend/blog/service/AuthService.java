package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.auth.*;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입
    public void signup(SignUpRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        String encoded = passwordEncoder.encode(req.getPassword());

        User user = new User(
                req.getEmail(),
                encoded,
                req.getName(),
                req.getNickname(),
                "ROLE_USER"
        );

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshTokenString = jwtTokenProvider.createRefreshToken(user.getEmail());

        // 기존 엔티티가 있으면 가져오고, 없으면 새 엔티티 생성
        RefreshToken tokenEntity = refreshTokenRepository.findByUser(user)
                .orElseGet(RefreshToken::new);

        tokenEntity.setToken(refreshTokenString);
        tokenEntity.setUser(user);
        tokenEntity.setExpiryDate(
                Instant.ofEpochMilli(jwtTokenProvider.getExpiration(refreshTokenString).getTime())
        );

        refreshTokenRepository.save(tokenEntity);

        long expiresIn = jwtTokenProvider.getExpiration(accessToken).getTime() - new Date().getTime();
        return new TokenResponse(accessToken, refreshTokenString, expiresIn);
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getSubject(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken stored = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        // NPE 방지: equals 호출은 외부(인자) 문자열에서 수행 -> 안전하게 비교
        if (!refreshToken.equals(stored.getToken()) || stored.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token invalid or expired");
        }

        String newAccess = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        long expiresIn = jwtTokenProvider.getExpiration(newAccess).getTime() - new Date().getTime();

        return new TokenResponse(newAccess, refreshToken, expiresIn);
    }

    // 로그아웃: 해당 유저의 리프레시 토큰 삭제
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }
}
