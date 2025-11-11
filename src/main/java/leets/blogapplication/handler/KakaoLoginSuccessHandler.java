package leets.blogapplication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leets.blogapplication.service.auth.RefreshTokenService;
import leets.blogapplication.service.auth.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class KakaoLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final RefreshTokenService refreshService;

    public KakaoLoginSuccessHandler(TokenService tokenService, RefreshTokenService refreshService) {
        this.tokenService = tokenService;
        this.refreshService = refreshService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException {
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        Object raw = principal.getAttribute("id");
        long kakaoId = ((Number) raw).longValue(); // 실패 시 ClassCastException로 바로 발견

        String refresh = tokenService.createNewRefreshToken(kakaoId);
        String access  = tokenService.createNewAccessTokenSocial(refresh);

        res.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access);
        res.setHeader("Access-Control-Expose-Headers", "Authorization");

        ResponseCookie cookie = ResponseCookie.from("REFRESH_TOKEN", refresh)
                .httpOnly(true)
                .secure(true)        // 운영 HTTPS 필수
                .sameSite("Lax")     // 프론트/백 도메인 분리면 "None"
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"accessToken\":\"" + access + "\"}");
        clearAuthenticationAttributes(req);
    }
}
