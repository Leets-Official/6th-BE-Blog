package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.kakao.KakaoLoginResponse;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;
import com.leets.backend.blog.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final long REFRESH_TOKEN_EXPIRY_SECONDS = 7 * 24 * 60 * 60;

    public KakaoAuthService(UserRepository userRepository,
                            RefreshTokenRepository refreshTokenRepository,
                            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * ① 프론트가 이동해야 할 카카오 로그인 URL 생성
     */
    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }

    /**
     * ② callback의 code로 Access Token 요청 → 사용자 정보 조회 → 회원가입/로그인 처리
     */
    public KakaoLoginResponse loginWithCode(String code) {

        // (1) code로 Access Token 요청
        Map<String, String> tokenResponse = requestAccessToken(code);
        String kakaoAccessToken = tokenResponse.get("access_token");

        // (2) 사용자 정보 요청
        Map<String, Object> userInfo = requestUserInfo(kakaoAccessToken);

        String kakaoId = String.valueOf(userInfo.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = kakaoAccount.get("email") != null
                ? kakaoAccount.get("email").toString()
                : kakaoId + "@kakao.com";

        String nickname = profile != null && profile.get("nickname") != null
                ? profile.get("nickname").toString()
                : "KakaoUser";

        // (3) DB 조회 or 회원가입
        User user = userRepository.findBySocialId(kakaoId).orElseGet(() -> {
            User newUser = new User();
            newUser.setSocialId(kakaoId);
            newUser.setEmail(email);
            newUser.setNickname(nickname);
            newUser.setProvider("kakao");
            newUser.setRole("ROLE_USER");
            return userRepository.save(newUser);
        });

        // (4) 자체 JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshTokenValue = jwtTokenProvider.createRefreshToken(user.getEmail());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS));

        refreshTokenRepository.save(refreshToken);

        return new KakaoLoginResponse(accessToken, refreshTokenValue, email, nickname);
    }

    /**
     * (A) code → Access Token 요청 (✅ 수정된 부분)
     */
    private Map<String, String> requestAccessToken(String code) {

        // 1. 헤더 설정: Content-Type을 x-www-form-urlencoded로 명시
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. 요청 본문(Body) 설정: HashMap 대신 MultiValueMap 사용
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);

        // 💡 client-secret 추가 (필수 항목은 아니지만 보안 강화를 위해 사용)
         params.add("client_secret", clientSecret);

        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // 3. HttpEntity 구성: 헤더와 본문을 담아 요청 객체 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody();
    }

    /**
     * (B) Access Token → 사용자 정보 요청
     */
    private Map<String, Object> requestUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody();
    }
}