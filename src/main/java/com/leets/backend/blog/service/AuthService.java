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
import com.leets.backend.blog.dto.UserSignUpRequestDto;
import com.leets.backend.blog.dto.UserLoginRequestDto;
// !! (리뷰 반영) 새로운 커스텀 예외 클래스 임포트 !!
import com.leets.backend.blog.exception.DuplicateDataException;
import com.leets.backend.blog.exception.InvalidCredentialsException;
import com.leets.backend.blog.exception.InvalidTokenException;
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
        // !! (리뷰 반영) DuplicateDataException으로 수정 !!
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateDataException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new DuplicateDataException("이미 사용 중인 닉네임입니다.");
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
     * 로그인 비즈니스 로직
     */
    @Transactional
    public TokenInfo login(UserLoginRequestDto requestDto) {
        // 1. 이메일로 사용자 조회
        // !! (리뷰 반영) InvalidCredentialsException으로 수정 !!
        // (보안을 위해 "가입되지 않은 이메일"과 "잘못된 비밀번호"를 동일한 예외 메시지로 처리)
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 2. 비밀번호 일치 여부 확인
        // !! (리뷰 반영) InvalidCredentialsException으로 수정 !!
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 3. 인증 성공: 토큰 생성
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), authorities, now);
        String refreshToken = jwtTokenProvider.createRefreshToken(now);

        // 4. Refresh Token을 DB에 저장
        RefreshToken rt = refreshTokenRepository.findByUserId(user.getId())
                .orElse(new RefreshToken(user.getId()));

        rt.updateToken(refreshToken);
        refreshTokenRepository.save(rt);

        // 5. 토큰 정보(TokenInfo) DTO로 반환
        return TokenInfo.of("Bearer", accessToken, refreshToken);
    }


    /**
     * 토큰 재발급(reissue) 서비스 메서드 구현
     */
    @Transactional
    public TokenInfo reissueToken(String requestRefreshToken) {

        // 1. Refresh Token 유효성 검증
        // !! (리뷰 반영) InvalidTokenException으로 수정 !!
        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");
        }

        // 2. DB에서 Refresh Token 조회
        // !! (리뷰 반영) InvalidTokenException으로 수정 !!
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new InvalidTokenException("서버에 존재하지 않는 Refresh Token입니다."));

        // 3. Token에 연동된 User 정보 조회
        // !! (리뷰 반영) InvalidTokenException으로 수정 !!
        // (UserNotFoundException을 던질 수도 있지만, "토큰 관련 오류"로 묶어서 401을 반환하는 것이 더 적절함)
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new InvalidTokenException("Token의 사용자 정보가 유효하지 않습니다."));

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