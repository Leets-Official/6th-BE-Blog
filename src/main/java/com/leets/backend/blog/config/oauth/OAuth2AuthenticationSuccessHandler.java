package com.leets.backend.blog.config.oauth;

import com.leets.backend.blog.config.jwt.JwtTokenProvider;
import com.leets.backend.blog.domain.RefreshToken;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository; // 3. [추가] UserRepository 필드

    // 4. [수정] 생성자에 UserRepository 주입
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider,
                                              RefreshTokenRepository refreshTokenRepository,
                                              UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found in DB after OAuth login: " + email));

        Long userId = user.getId();
        String role = user.getRoleKey();

        log.info("OAuth2 login successful. Issuing JWT for User ID: {}, Email: {}, Role: {}", userId, email, role);

        String accessToken = jwtTokenProvider.createAccessToken(email, role, userId);

        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        RefreshToken refreshTokenEntity = new RefreshToken(userId, refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        String targetUrl = buildRedirectUrl(accessToken, refreshToken);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String buildRedirectUrl(String accessToken, String refreshToken) {
        String frontendCallbackUrl = "http://localhost:3000/auth/redirect";

        return UriComponentsBuilder.fromUriString(frontendCallbackUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }
}