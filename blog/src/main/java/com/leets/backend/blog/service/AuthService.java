package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.LoginRequestDTO;
import com.leets.backend.blog.dto.SignUpRequestDTO;
import com.leets.backend.blog.dto.TokenResponseDTO;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.enums.LoginMethod;
import com.leets.backend.blog.exception.auth.ErrorCode;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;
import jakarta.security.auth.message.AuthException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final long refreshTokenExpirationMs;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, com.leets.backend.blog.config.JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenExpirationMs = jwtProperties.getRefreshTokenExpirationMs();
    }

    // 이메일 회원가입
    public User signUp(SignUpRequestDTO request) throws AuthException {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(String.valueOf(ErrorCode.EMAIL_ALREADY_EXISTS));
        }
        // 닉네임 중복 확인
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new AuthException(String.valueOf(ErrorCode.NICKNAME_ALREADY_EXISTS));
        }

        User user = new User(
                request.getEmail(), // 이메일 주소
                passwordEncoder.encode(request.getPassword()), // 비밀번호
                request.getName(), // 이름
                request.getNickname(), // 닉네임
                request.getProfileImage(), // 프로필 사진
                LoginMethod.EMAIL, // 로그인 방식
                request.getIntroduction(), // 한 줄 소개
                request.getBirthdate(), // 생년월일
                "ROLE_USER"
        );

        return userRepository.save(user);
    }

    // 이메일 로그인
    public TokenResponseDTO login(LoginRequestDTO request) {
        // Spring Security의 AuthenticationManager를 통해 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 인증 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // User 객체 가져오기
        User user = (User) authentication.getPrincipal();

        // JWT 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshTokenString = jwtTokenProvider.createRefreshToken(authentication);

        // Refresh Token DB에 저장 (기존 토큰 삭제 후)
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = new RefreshToken(
                user,
                refreshTokenString,
                LocalDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000L) // ms to ns and add
        );
        refreshTokenRepository.save(refreshToken);

        return new TokenResponseDTO(accessToken, refreshTokenString);
    }

    // 로그아웃
    public void logout(String refreshToken) throws AuthException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AuthException(String.valueOf(ErrorCode.INVALID_REFRESH_TOKEN));
        }

        // DB에서 Refresh Token 조회 및 삭제
        RefreshToken dbRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException(String.valueOf(ErrorCode.REFRESH_TOKEN_NOT_FOUND)));

        refreshTokenRepository.delete(dbRefreshToken);
    }

    // 토큰 재발급
    public TokenResponseDTO refresh(String refreshToken) throws AuthException {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AuthException(String.valueOf(ErrorCode.INVALID_REFRESH_TOKEN));
        }

        // Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthException(String.valueOf(ErrorCode.INVALID_REFRESH_TOKEN));
        }

        // DB에서 Refresh Token 조회
        RefreshToken dbRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException(String.valueOf(ErrorCode.REFRESH_TOKEN_NOT_FOUND)));

        // 토큰 만료 시간 확인
        if (dbRefreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(dbRefreshToken);
            throw new AuthException(String.valueOf(ErrorCode.EXPIRED_REFRESH_TOKEN));
        }

        // 토큰에서 email 추출
        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(String.valueOf(ErrorCode.USER_NOT_FOUND)));

        // 새로운 Access Token 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);

        return new TokenResponseDTO(newAccessToken);
    }
}
