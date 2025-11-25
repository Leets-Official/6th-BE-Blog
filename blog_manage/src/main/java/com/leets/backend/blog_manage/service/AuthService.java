package com.leets.backend.blog_manage.service;

// (기존 imports)
import com.leets.backend.blog_manage.dto.auth.*;
import com.leets.backend.blog_manage.entity.RefreshToken;
import com.leets.backend.blog_manage.entity.User;
import com.leets.backend.blog_manage.exception.CustomException;
import com.leets.backend.blog_manage.exception.ErrorCode;
import com.leets.backend.blog_manage.repository.RefreshTokenRepository;
import com.leets.backend.blog_manage.repository.UserRepository;
import com.leets.backend.blog_manage.security.jwt.JwtTokenProvider;
import com.leets.backend.blog_manage.security.service.CustomUserDetails; // (CustomUserDetails import)
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity; // (HttpEntity import)
import org.springframework.http.HttpHeaders; // (HttpHeaders import)
import org.springframework.http.HttpMethod; // (HttpMethod import)
import org.springframework.http.ResponseEntity; // (ResponseEntity import)
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap; // (MultiValueMap imports)
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate; // (RestTemplate import)

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

    // --- [카카오 로그인 관련 필드 추가] ---
    private final String kakaoClientId;
    private final String kakaoRedirectUri;
    private final String kakaoTokenUri;
    private final String kakaoUserInfoUri;
    private final RestTemplate restTemplate;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationMillis,
                       // --- [카카오 주입] ---
                       @Value("${kakao.client-id}") String kakaoClientId,
                       @Value("${kakao.redirect-uri}") String kakaoRedirectUri,
                       @Value("${kakao.token-uri}") String kakaoTokenUri,
                       @Value("${kakao.user-info-uri}") String kakaoUserInfoUri) {

        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        // --- [카카오 필드 초기화] ---
        this.kakaoClientId = kakaoClientId;
        this.kakaoRedirectUri = kakaoRedirectUri;
        this.kakaoTokenUri = kakaoTokenUri;
        this.kakaoUserInfoUri = kakaoUserInfoUri;
        this.restTemplate = new RestTemplate(); // RestTemplate 생성
    }

    // 1. (기존) 이메일 회원가입
    @Transactional
    public User signUp(SignUpRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        User user = request.toEntity(passwordEncoder);
        return userRepository.save(user);
    }

    // 2. (기존) 이메일 로그인
    @Transactional
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshTokenString = createAndSaveRefreshToken(authentication);
        addRefreshTokenToCookie(response, refreshTokenString);
        return new TokenResponse(accessToken);
    }

    // --- [3. 카카오 로그인/회원가입 분기 처리] ---
    @Transactional
    public KakaoLoginResponse kakaoLogin(KakaoLoginRequest request, HttpServletResponse response) {
        // 1. 프론트에서 받은 인증 코드로 카카오 Access Token 받기
        String kakaoAccessToken = getKakaoAccessToken(request.getAuthorizationCode());

        // 2. 카카오 Access Token으로 카카오 사용자 정보 받기 (오직 ID만)
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(kakaoAccessToken);

        // 3. 카카오 ID로 DB에서 사용자 조회
        String kakaoId = userInfo.getId();
        User user = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (user != null) {
            // 4-1. (로그인) 이미 가입된 사용자
            // User 객체로 바로 Authentication 객체 생성
            Authentication authentication = createAuthentication(user);

            TokenResponse tokenResponse = loginSocialUser(authentication, response);

            // 로그인 성공 응답
            return new KakaoLoginResponse("LOGIN_SUCCESS", tokenResponse, null);

        } else {
            // 4-2. (회원가입 필요) 신규 사용자 (피그마 150600.jpg 모달)
            // 프론트에 회원가입에 필요한 kakaoId만 담아 반환
            KakaoSignupInfo signupInfo = new KakaoSignupInfo(kakaoId);

            // 회원가입 필요 응답
            return new KakaoLoginResponse("SIGNUP_REQUIRED", null, signupInfo);
        }
    }

    // --- [4. 카카오 회원가입] ---
    @Transactional
    public TokenResponse kakaoSignUp(KakaoSignUpRequest request, HttpServletResponse response) {
        // 1. 이메일 중복 확인 (사용자가 직접 입력한 값)
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        // 2. 닉네임 중복 확인 (사용자가 직접 입력한 값)
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // 3. DTO -> Entity 변환 (비밀번호는 임의값)
        User user = request.toEntity(passwordEncoder);
        User savedUser = userRepository.save(user);

        // 4. 회원가입과 동시에 로그인 처리 (토큰 발급)
        Authentication authentication = createAuthentication(savedUser);
        TokenResponse tokenResponse = loginSocialUser(authentication, response);

        return tokenResponse;
    }


    // --- [카카오 API 통신 헬퍼 메소드] ---

    // 1. 인증 코드로 Access Token 받기
    private String getKakaoAccessToken(String authorizationCode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoClientId);
            params.add("redirect_uri", kakaoRedirectUri);
            params.add("code", authorizationCode);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                    kakaoTokenUri,
                    request,
                    KakaoTokenResponse.class
            );

            return response.getBody().getAccessToken();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.KAKAO_AUTH_ERROR);
        }
    }

    // 2. Access Token으로 사용자 정보 받기 (오직 ID만)
    private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // (중요) 카카오 API에 "property_keys" 파라미터를 빈 값으로 보내면
            // id만 반환하도록 요청할 수 있으나, 기본 GET 요청만 해도 id는 항상 포함됩니다.
            // 여기서는 KakaoUserInfoResponse DTO 자체에 id 필드만 정의했으므로
            // Jackson이 다른 필드(kakao_account 등)는 자동으로 무시합니다.

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
            ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                    kakaoUserInfoUri,
                    HttpMethod.GET,
                    request,
                    KakaoUserInfoResponse.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.KAKAO_AUTH_ERROR);
        }
    }

    // --- [기존 헬퍼 메소드] ---

    // (기존) RefreshToken 생성 및 저장
    private String createAndSaveRefreshToken(Authentication authentication) {
        String refreshTokenString = jwtTokenProvider.createRefreshToken(authentication);
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        LocalDateTime expiryDate = LocalDateTime.now().plus(refreshTokenExpirationMillis, ChronoUnit.MILLIS);
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder().user(user).build());
        refreshToken.updateToken(refreshTokenString, expiryDate);
        refreshTokenRepository.save(refreshToken);
        return refreshTokenString;
    }

    // (기존) RefreshToken 쿠키에 추가
    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        long maxAgeInSeconds = refreshTokenExpirationMillis / 1000;
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAgeInSeconds);
        response.addCookie(cookie);
    }

    // --- [신규 헬퍼 메소드] ---

    // 3. 소셜 로그인 사용자를 위한 토큰 발급
    // (이메일 로그인과 달리 AuthenticationManager를 거치지 않으므로, Authentication 객체를 받아 바로 토큰 발급)
    @Transactional
    private TokenResponse loginSocialUser(Authentication authentication, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshTokenString = createAndSaveRefreshToken(authentication);
        addRefreshTokenToCookie(response, refreshTokenString);
        return new TokenResponse(accessToken);
    }

    // 4. User 객체로 Authentication 객체 생성
    // (CustomUserDetailsService를 타지 않고 바로 Authentication을 생성하기 위함)
    private Authentication createAuthentication(User user) {
        // CustomUserDetails가 UserDetails를 구현하고 있으므로 사용
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(
                userDetails, // principal
                null, // credentials (비밀번호 없음)
                userDetails.getAuthorities() // authorities
        );
    }
}