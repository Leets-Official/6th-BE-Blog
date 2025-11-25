package com.leets.backend.blog.config.oauth;

import com.leets.backend.blog.config.jwt.JwtTokenProvider;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.service.CustomOAuth2UserService;
import com.leets.backend.blog.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 1. [추가] 리다이렉트 URI를 담을 필드 선언
    private final String redirectUri;

    // 2. [수정] 생성자에 @Value를 사용하여 프로퍼티 값 주입
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider,
                                              RefreshTokenService refreshTokenService,
                                              CustomOAuth2UserService customOAuth2UserService,
                                              @Value("${app.oauth2.redirect-uri}") String redirectUri) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (kakaoAccount == null) {
            log.error("Kakao account attributes are missing.");
            getRedirectStrategy().sendRedirect(request, response, "/loginFailure?error=KakaoAccountMissing");
            return;
        }

        String email = (String) kakaoAccount.get("email");

        if (email == null) {
            log.error("Email not found from OAuth2 provider");
            getRedirectStrategy().sendRedirect(request, response, "/loginFailure?error=EmailNotFound");
            return;
        }

        User user = customOAuth2UserService.getUserByEmail(email);

        Long userId = user.getId();
        String role = user.getRoleKey();

        log.info("OAuth2 login successful. Issuing JWT for User ID: {}, Email: {}, Role: {}", userId, email, role);

        String accessToken = jwtTokenProvider.createAccessToken(email, role, userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        refreshTokenService.saveRefreshToken(userId, refreshToken);

        String targetUrl = buildRedirectUrl(accessToken, refreshToken);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String buildRedirectUrl(String accessToken, String refreshToken) {
        // 3. [수정] 하드코딩된 문자열 대신 주입받은 필드(redirectUri) 사용
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }
}