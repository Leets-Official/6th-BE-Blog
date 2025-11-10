package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.LoginRequestDTO;
import com.leets.backend.blog.dto.SignUpRequestDTO;
import com.leets.backend.blog.dto.TokenResponseDTO;
import com.leets.backend.blog.dto.kakao.KakaoAccount;
import com.leets.backend.blog.dto.kakao.KakaoTokenResponseDTO;
import com.leets.backend.blog.dto.kakao.KakaoUserInfoResponseDTO;
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
    private final KakaoOAuthService kakaoOAuthService;


    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, com.leets.backend.blog.config.JwtProperties jwtProperties
    ,KakaoOAuthService kakaoOAuthService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenExpirationMs = jwtProperties.getRefreshTokenExpirationMs();
        this.kakaoOAuthService = kakaoOAuthService;
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
                LocalDateTime.now().plusNanos(refreshTokenExpirationMs / 1000) // ms to ns and add
        );
        refreshTokenRepository.save(refreshToken);

        return new TokenResponseDTO(accessToken, refreshTokenString);
    }

    // 카카오 로그인/회원가입
    public TokenResponseDTO loginWithKakao(String code) throws AuthException {
        // 카카오 서버로부터 사용자 정보 획득
        KakaoTokenResponseDTO kakaoToken = kakaoOAuthService.getKakaoToken(code);
        KakaoUserInfoResponseDTO userInfo = kakaoOAuthService.getKakaoUserInfo(kakaoToken.getAccessToken());

        String kakaoId = userInfo.getId().toString();
        KakaoAccount kakaoAccount = userInfo.getKakaoAccount();

        String email = kakaoAccount.getEmail();
        if (email == null) {
            throw new AuthException(String.valueOf(ErrorCode.KAKAO_EMAIL_NOT_CONSENTED));
        }

        // 카카오 ID로 DB에서 사용자 조회
        User user = userRepository.findByKakaoId(kakaoId)
                .map(existingUser -> {
                    // 기존 사용자인 경우: 정보 업데이트
                    existingUser.updateKakaoProfile(kakaoAccount);
                    return existingUser;
                })
                .orElseGet(() -> {
                    // 신규 사용자인 경우: 회원가입
                    // 이메일 중복 체크 (로컬 계정과 중복될 경우)
                    if (userRepository.existsByEmail(email)) {
                        try {
                            throw new AuthException(String.valueOf(ErrorCode.EMAIL_ALREADY_EXISTS));
                        } catch (AuthException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // name 필드
                    String lastDigits = kakaoId.substring(Math.max(0, kakaoId.length() - 6));
                    String tempName = "K_" + lastDigits; // 예: K_386990 (총 8자)
                    // ---------------------------------

                    // 닉네임 처리
                    String kakaoNickname = kakaoAccount.getProfile() != null ? kakaoAccount.getProfile().getNickname() : null;
                    // 닉네임이 없으면 카카오 ID 기반으로 임시 닉네임 생성
                    String baseNickname = (kakaoNickname != null && !kakaoNickname.isEmpty()) ? kakaoNickname : "user_" + kakaoId;

                    // 고유 닉네임 확보
                    String uniqueNickname = getUniqueNickname(baseNickname);

                    String profileImageUrl = kakaoAccount.getProfile() != null ? kakaoAccount.getProfile().getProfileImageUrl() : null;

                    User newUser = User.createKakaoUser(kakaoId, email, tempName, uniqueNickname, profileImageUrl);
                    return userRepository.save(newUser);
                });

        // JWT 발급
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, user.getAuthorities()
        );

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshTokenString = jwtTokenProvider.createRefreshToken(authentication);

        // Refresh Token DB 저장
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = new RefreshToken(
                user,
                refreshTokenString,
                LocalDateTime.now().plusNanos(refreshTokenExpirationMs / 1000)
        );
        refreshTokenRepository.save(refreshToken);

        return new TokenResponseDTO(accessToken, refreshTokenString);
    }

    private String getUniqueNickname(String nickname) {
        String uniqueNickname = nickname;
        int suffix = 1;
        // DB에 중복된 닉네임이 없을 때까지 반복
        while (userRepository.existsByNickname(uniqueNickname)) {
            uniqueNickname = nickname + "_" + suffix;
            suffix++;
        }
        return uniqueNickname;
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
