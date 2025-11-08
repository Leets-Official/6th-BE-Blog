package com.leets.backend.blog_manage.service;

import com.leets.backend.blog_manage.dto.auth.LoginRequest;
import com.leets.backend.blog_manage.dto.auth.SignUpRequest;
import com.leets.backend.blog_manage.dto.auth.TokenResponse;
import com.leets.backend.blog_manage.entity.RefreshToken;
import com.leets.backend.blog_manage.entity.User;
import com.leets.backend.blog_manage.exception.CustomException;
import com.leets.backend.blog_manage.exception.ErrorCode;
import com.leets.backend.blog_manage.repository.RefreshTokenRepository;
import com.leets.backend.blog_manage.repository.UserRepository;
import com.leets.backend.blog_manage.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final long refreshTokenExpirationMillis;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationMillis) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
    }

    // 1. 회원가입
    @Transactional
    public User signUp(SignUpRequest request) {
        // 비밀번호 일치 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        // 닉네임 중복 확인
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        User user = request.toEntity(passwordEncoder);
        return userRepository.save(user);
    }

    // 2. 로그인 (리팩토링됨)
    @Transactional
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {
        // 1. Login ID/PW를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // 2. 실제 검증 (CustomUserDetailsService 사용)
        Authentication authentication;
        try {
            // 주입받은 authenticationManager 사용
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            // 인증 실패
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 3. 인증 정보를 기반으로 AccessToken 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        // 4. RefreshToken 생성 및 DB 저장 (별도 메소드로 분리)
        String refreshTokenString = createAndSaveRefreshToken(authentication);

        // 5. RefreshToken을 HttpOnly Secure 쿠키에 담아 응답
        addRefreshTokenToCookie(response, refreshTokenString);

        // 6. AccessToken은 응답 본문에 담아 반환
        return new TokenResponse(accessToken);
    }

    // [추가] RefreshToken 생성 및 저장을 담당하는 private 메소드
    private String createAndSaveRefreshToken(Authentication authentication) {
        // Refresh Token 생성
        String refreshTokenString = jwtTokenProvider.createRefreshToken(authentication);

        // User 조회
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 만료 시간 설정
        LocalDateTime expiryDate = LocalDateTime.now().plus(refreshTokenExpirationMillis, ChronoUnit.MILLIS);

        // RefreshToken 조회 및 업데이트 (없으면 새로 생성)
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder().user(user).build());

        refreshToken.updateToken(refreshTokenString, expiryDate);
        refreshTokenRepository.save(refreshToken);

        return refreshTokenString;
    }

    // RefreshToken을 쿠키에 추가하는 헬퍼 메소드
    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        // 쿠키 만료 시간을 초 단위로 설정
        long maxAgeInSeconds = refreshTokenExpirationMillis / 1000;

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true); // JS에서 접근 불가
        cookie.setSecure(true); // HTTPS에서만 전송 (운영 환경)
        cookie.setPath("/"); // 사이트 전체에서 사용
        cookie.setMaxAge((int) maxAgeInSeconds); // 쿠키 만료 시간 설정

        // SameSite=Strict (혹은 Lax) 설정으로 CSRF 방어
        // Spring 5.1+ 에서는 ResponseCookie 빌더 사용 권장되나, 기본 Cookie로도 설정 가능
        // cookie.setAttribute("SameSite", "Strict");
        // (참고: Spring Boot 2.7+ 에서는 application.yml에서 server.servlet.cookie.same-site=strict 설정 가능)

        response.addCookie(cookie);
    }
}