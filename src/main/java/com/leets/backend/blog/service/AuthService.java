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
// [추가] 로그인 DTO import
import com.leets.backend.blog.dto.UserLoginRequestDto;
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
     * [추가된 로그인 메소드]
     * 로그인 비즈니스 로직
     */
    @Transactional
    public TokenInfo login(UserLoginRequestDto requestDto) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 일치 여부 확인
        // (requestDto.getPassword(): 사용자가 입력한 원본 비밀번호, user.getPassword(): DB에 저장된 암호화된 비밀번호)
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 3. 인증 성공: 토큰 생성
        // 3-1. 사용자의 권한 정보(roles)를 문자열로 변환 (reissueToken 로직과 동일)
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // 3-2. Access Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), authorities, now);

        // 3-3. Refresh Token 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(now);

        // 4. Refresh Token을 DB에 저장 (사용자 ID와 매핑)
        //    (기존에 토큰이 있다면 새 토큰으로 덮어쓰기/업데이트)
        RefreshToken rt = refreshTokenRepository.findByUserId(user.getId())
                .orElse(new RefreshToken(user.getId())); // 새 RefreshToken 객체 생성 (userId만 설정)

        rt.updateToken(refreshToken); // 새 Refresh Token 값으로 업데이트
        refreshTokenRepository.save(rt); // DB에 저장 (insert or update)

        // 5. 토큰 정보(TokenInfo) DTO로 반환
        return TokenInfo.of("Bearer", accessToken, refreshToken);
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