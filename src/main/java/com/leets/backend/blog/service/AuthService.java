package com.leets.backend.blog.service;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.backend.blog.config.jwt.JwtTokenProvider;
import com.leets.backend.blog.domain.RefreshToken;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.dto.TokenInfo;
import com.leets.backend.blog.dto.UserSignUpRequestDto; // 이 import는 이제 사용되지 않지만, 있어도 문제는 없습니다.
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 회원가입 비즈니스 로직
     */
    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setNickname(requestDto.getNickname());

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    /**
     * 9. 토큰 재발급(reissue) 서비스 메서드 구현
     * @param requestRefreshToken (String 토큰 값)
     * @return 갱신된 TokenInfo (새 Access Token + 기존 Refresh Token)
     */
    @Transactional
    public TokenInfo reissueToken(String requestRefreshToken) { // <- 파라미터를 DTO가 아닌 String으로 변경

        // 1. Refresh Token 유효성 검증 (JWT 자체의 유효성)
        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        // 2. DB에서 Refresh Token 조회 (findByToken 메서드 사용)
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("서버에 존재하지 않는 Refresh Token입니다."));

        // 3. Token에 연동된 User 정보 조회
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Token의 사용자 정보가 유효하지 않습니다."));

        // 4. 새로운 Access Token 생성
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), authorities, now);

        // 5. 새 Access Token과 기존 Refresh Token으로 TokenInfo 반환
        return TokenInfo.of("Bearer", newAccessToken, requestRefreshToken);
    }
}

