package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.kakao.KakaoLoginResponse;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;
import com.leets.backend.blog.config.JwtTokenProvider;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Service
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    // 예시: Refresh Token 유효기간 (7일)
    private static final long REFRESH_TOKEN_EXPIRY_SECONDS = 7 * 24 * 60 * 60;

    public KakaoAuthService(UserRepository userRepository,
                            RefreshTokenRepository refreshTokenRepository,
                            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restTemplate = new RestTemplate();
    }

    public KakaoLoginResponse kakaoLogin(String kakaoAccessToken) {

        // 카카오 API 요청
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 사용자 정보 요청 실패 (status=" + response.getStatusCode() + ")");
        }

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("카카오 사용자 정보가 비어 있습니다.");
        }

        String kakaoId = String.valueOf(body.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : kakaoId + "@kakao.com";
        String nickname = (profile != null && profile.get("nickname") != null)
                ? profile.get("nickname").toString()
                : "KakaoUser";

        // 사용자 조회/등록
        User user = userRepository.findBySocialId(kakaoId).orElseGet(() -> {
            User newUser = new User();
            newUser.setSocialId(kakaoId);
            newUser.setEmail(email);
            newUser.setNickname(nickname);
            newUser.setProvider("kakao");
            newUser.setRole("ROLE_USER");
            return userRepository.save(newUser);
        });

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshTokenValue = jwtTokenProvider.createRefreshToken(user.getEmail());

        // Refresh Token 저장 (기존 토큰 삭제 후 갱신)
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS));

        refreshTokenRepository.save(refreshToken);

        System.out.println(" RefreshToken 저장 완료: " + refreshTokenValue);

        return new KakaoLoginResponse(accessToken, refreshTokenValue, email, nickname);
    }
}
