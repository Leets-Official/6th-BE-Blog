package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.AppException;
import com.leets.backend.blog.exception.ErrorCode;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private static final String REFRESH_COOKIE_NAME = "REFRESH_TOKEN";

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 로그인 시 사용자에게 새 RefreshToken을 발급하고 DB에 저장한다.
     * (유저당 항상 1개의 행만 유지)
     */
    @Transactional
    public String issueRefreshToken(User user) {
        String token = jwtTokenProvider.generateRefreshToken(user.getUserId());

        RefreshToken rt = refreshTokenRepository.findByUser_UserId(user.getUserId())
                .orElseGet(RefreshToken::new);

        rt.setUser(user);
        rt.setToken(token);
        rt.setExpiresAt(LocalDateTime.now().plusDays(14)); // 필요하면 설정값으로 분리
        rt.setRevoked(false);

        refreshTokenRepository.save(rt);
        return token;
    }

    /**
     * RefreshToken 쿠키 값을 가져온다.
     */
    public Optional<String> extractRefreshTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> REFRESH_COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    /**
     * 문자열 토큰으로 DB에 저장된 RefreshToken을 조회하고,
     * 만료/회수 여부를 검증한다.
     */
    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken stored = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new AppException(ErrorCode.VALIDATION_ERROR, "refresh not found"));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "refresh expired/revoked");
        }

        return stored;
    }

    /**
     * 기존 RefreshToken 엔티티를 이용해 새 토큰으로 로테이션한다.
     * (행은 그대로, token/만료시간만 갱신)
     */
    @Transactional
    public String rotateRefreshToken(RefreshToken stored) {
        User user = stored.getUser();
        String newToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        stored.setToken(newToken);
        stored.setExpiresAt(LocalDateTime.now().plusDays(14));
        stored.setRevoked(false);

        refreshTokenRepository.save(stored);
        return newToken;
    }

    /**
     * 주어진 토큰을 revoke 처리한다.
     */
    @Transactional
    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(rt -> {
                    rt.setRevoked(true);
                    refreshTokenRepository.save(rt);
                });
    }

    /**
     * RefreshToken 쿠키를 내려준다.
     */
    public void addRefreshTokenCookie(String token, HttpServletResponse res) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);   // 🔸 운영 환경에서는 true
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 60 * 60);
        res.addCookie(cookie);
    }

    /**
     * RefreshToken 쿠키를 삭제한다.
     */
    public void clearRefreshTokenCookie(HttpServletResponse res) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);   // 🔸 운영 환경에서는 true
        cookie.setPath("/");
        cookie.setMaxAge(0);       // 즉시 만료
        res.addCookie(cookie);
    }
}
