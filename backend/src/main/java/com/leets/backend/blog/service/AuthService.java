package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.AuthResponse;
import com.leets.backend.blog.dto.LoginRequest;
import com.leets.backend.blog.dto.SignupRequest;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.AppException;
import com.leets.backend.blog.exception.ErrorCode;
import com.leets.backend.blog.exception.UserNotFoundException;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    // 회원가입 - 이메일 전용
    @Transactional
    public void signup(SignupRequest req){
        if (userRepository.existsByEmail(req.email())) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(req.nickname())) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "이미 사용 중인 닉네임입니다.");
        }
        User user = new User();
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setNickname(req.nickname());
        user.setProvider("email");
        user.setRole("USER");
        userRepository.save(user);
    }

    // 로그인 - 이메일 로그인
    @Transactional
    public AuthResponse login(LoginRequest req, HttpServletResponse res){
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new UserNotFoundException(0L));

        if (!"email".equals(user.getProvider()))
            throw new AppException(ErrorCode.VALIDATION_ERROR, "이메일 로그인 대상이 아닙니다.");

        if (!passwordEncoder.matches(req.password(), user.getPassword()))
            throw new AppException(ErrorCode.VALIDATION_ERROR, "비밀번호가 일치하지 않습니다.");

        // access 발급
        String access = jwtTokenProvider.generateAccessToken(user.getUserId(), user.getEmail(), user.getRole());

        // refresh 발급 & 저장
        String refresh = refreshTokenService.issueRefreshToken(user);


        // refresh 쿠키 세팅 (HttpOnly, Secure)
        refreshTokenService.addRefreshTokenCookie(refresh, res);

        return AuthResponse.of(access, user);
    }

    @Transactional
    public AuthResponse refresh(HttpServletRequest req, HttpServletResponse res){
        // 1. 쿠키에서 refresh 추출
        String refresh = refreshTokenService.extractRefreshTokenFromCookie(req)
                .orElseThrow(() ->
                        new AppException(ErrorCode.VALIDATION_ERROR, "no refresh cookie"));

        // 2. DB에서 검증
        RefreshToken stored = refreshTokenService.validateRefreshToken(refresh);
        User user = stored.getUser();

        // 3. 로테이션: 같은 엔티티로 새 토큰으로 교체
        String newRefresh = refreshTokenService.rotateRefreshToken(stored);

        // 4. 쿠키 갱신
        refreshTokenService.addRefreshTokenCookie(newRefresh, res);

        // 5. 새 access 발급
        String access = jwtTokenProvider.generateAccessToken(
                user.getUserId(), user.getEmail(), user.getRole()
        );
        res.setHeader("Authorization", "Bearer " + access);

        return AuthResponse.of(access, user);
    }

    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res){
        // 1. 쿠키에서 refresh 추출
        String refresh = refreshTokenService.extractRefreshTokenFromCookie(req)
                .orElseThrow(() ->
                        new AppException(ErrorCode.VALIDATION_ERROR, "no refresh cookie"));

        // 2. 토큰 revoke
        refreshTokenService.revokeRefreshToken(refresh);

        // 3. 쿠키 삭제
        refreshTokenService.clearRefreshTokenCookie(res);
    }
}
