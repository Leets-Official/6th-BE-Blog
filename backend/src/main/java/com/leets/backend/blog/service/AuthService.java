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
        String refresh = jwtTokenProvider.generateRefreshToken(user.getUserId());

        // refresh 토큰 저장
        RefreshToken token = refreshTokenRepository.findByUser_UserId(user.getUserId())
                .orElseGet(RefreshToken::new);
        token.setUser(user);
        token.setToken(refresh);
        token.setExpiresAt(LocalDateTime.now().plusDays(14));
        token.setRevoked(false);
        refreshTokenRepository.save(token);

        // refresh 쿠키 세팅 (HttpOnly, Secure)
        Cookie cookie = new Cookie("REFRESH_TOKEN", refresh);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");         // 필요시 "/auth"로 좁히기
        cookie.setMaxAge(14*24*60*60);
        res.addCookie(cookie);

        res.setHeader("Authorization", "Bearer " + access);

        return AuthResponse.of(access, user);
    }

    @Transactional
    public AuthResponse refresh(HttpServletRequest req, HttpServletResponse res){
        String refresh = extractRefreshCookie(req)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "refresh할 쿠키가 존재하지 않습니다."));

        // DB에 존재 및 revoked = 0 인지 확인
        RefreshToken stored = refreshTokenRepository.findByToken(refresh)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "refresh가 존재하지 않습니다."));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "토큰이 이미 사용되었거나 만료되었습니다.");
        }

        Long userId = jwtTokenProvider.getUserId(refresh);
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 리프레시 로테이션: 새 refresh 재발급 + 기존 revoked=true
        String newRefresh = jwtTokenProvider.generateRefreshToken(u.getUserId());
        stored.setToken(newRefresh);
        stored.setExpiresAt(LocalDateTime.now().plusDays(14));
        stored.setRevoked(false);
        refreshTokenRepository.save(stored);

        // 쿠키 갱신
        Cookie cookie = new Cookie("REFRESH_TOKEN", newRefresh);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 배포시에는 true로 바꿔야됨
        cookie.setPath("/");
        cookie.setMaxAge(14*24*60*60); //14일
        res.addCookie(cookie);

        // access 새로 발급
        String access = jwtTokenProvider.generateAccessToken(u.getUserId(), u.getEmail(), u.getRole());
        res.setHeader("Authorization", "Bearer " + access);
        return AuthResponse.of(access, u);
    }

    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res){
        // 쿠키에서 refresh token 추출
        String refresh = extractRefreshCookie(req)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "refresh할 쿠키가 존재하지 않습니다."));

        // DB에서 해당 토큰 검색
        RefreshToken stored = refreshTokenRepository.findByToken(refresh)
                .orElseThrow(() -> new AppException(ErrorCode.VALIDATION_ERROR, "토큰이 검색되지 않습니다."));

        // 이미 사용되었거나 만료되었으면 예외
        if (stored.isRevoked() || stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "토큰이 이미 사용되었거나 만료되었습니다.");
        }

        // 현재 토큰을 무효화(revoke)
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        // 브라우저 쿠키 삭제
        Cookie cookie = new Cookie("REFRESH_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 배포시에는 true로 바꿔야됨
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        res.addCookie(cookie);
    }

    private Optional<String> extractRefreshCookie(HttpServletRequest req){
        Cookie[] cs = req.getCookies();
        if (cs == null) return Optional.empty();
        for (Cookie c : cs) {
            if ("REFRESH_TOKEN".equals(c.getName())) return Optional.ofNullable(c.getValue());
        }
        return Optional.empty();
    }
}
