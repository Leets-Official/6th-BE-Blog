package com.leets.backend.blog.auth.service;

import com.leets.backend.blog.auth.controller.dto.request.SignupRequest;
import com.leets.backend.blog.auth.controller.dto.response.SignupResponse;
import com.leets.backend.blog.auth.controller.dto.response.TokenResponse;
import com.leets.backend.blog.auth.entity.RefreshToken;
import com.leets.backend.blog.auth.repository.RefreshTokenRepository;
import com.leets.backend.blog.auth.validator.EmailNicknameValidator;
import com.leets.backend.blog.common.config.jwt.JwtProvider;
import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import com.leets.backend.blog.login.controller.dto.request.LoginRequest;
import com.leets.backend.blog.user.entity.User;
import com.leets.backend.blog.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailNicknameValidator validator;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StorageService storageService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailNicknameValidator validator,
                       JwtProvider jwtProvider,
                       RefreshTokenRepository refreshTokenRepository,
                       StorageService storageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.storageService = storageService;
    }

    public String saveProfileImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null; // 이미지가 없어도 회원가입 가능
        }
        return storageService.upload(file);
    }

    // 회원가입 로직
    public SignupResponse signup(SignupRequest signupRequest, String profileImageUrl) {
        validator.validateEmail(signupRequest.getEmail());
        validator.validateNickname(signupRequest.getNickname());

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User(
                signupRequest.getEmail(),
                encodedPassword,
                signupRequest.getNickname(),
                profileImageUrl
        );

        User saved = userRepository.save(user);
        return SignupResponse.from(saved);
    }

    // 이메일 로그인 로직
    public TokenResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "등록되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "등록되지 않은 이메일입니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail(), user.getId());

        saveOrUpdateRefreshToken(user, refreshToken);

        return new TokenResponse(accessToken, refreshToken, user.getNickname(), user.getId());
    }

    // 토큰 재발급 로직
    @Transactional
    public TokenResponse reissue(String refreshTokenClient) {
        if (!jwtProvider.validateToken(refreshTokenClient)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED, "유효하지 않은 Refresh Token입니다.");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshTokenClient)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH, "DB에 존재하지 않는 Refresh Token입니다."));

        User user = storedToken.getUser();

        String newAccessToken = jwtProvider.createAccessToken(user.getEmail(), user.getId());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getEmail(), user.getId());

        storedToken.updateToken(newRefreshToken, jwtProvider.getRefreshTokenExpiry());

        return new TokenResponse(newAccessToken, newRefreshToken, user.getNickname(), user.getId());
    }

    // 로그아웃 로직
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
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
