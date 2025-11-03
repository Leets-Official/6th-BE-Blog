package leets.blogapplication.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.time.Duration;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String REFRESH_TOKEN_COOKIE_PATH = "/";

    private final TokenProvider tokenProvider;
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 액세스 토큰 TTL (리프레시로 재발급 시 사용)
    private final Duration accessTokenTtl = Duration.ofMinutes(30);

    public TokenAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private static final String[] WHITELIST = {
            "/auth/login",
            "/auth/signup",
            "/comments/**",
            "/post/**",
            "/swagger-ui/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        for (String pattern : WHITELIST) {
            if (antPathMatcher.match(pattern, path)) return true; // ↩️ 이러면 doFilterInternal 아예 안 탐
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken = extractAccessToken(authorizationHeader);

        try {
            if (accessToken != null && tokenProvider.validToken(accessToken)) {
                Authentication auth = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                // 액세스 토큰이 없거나 invalid → 리프레시 시도
                String refreshToken = getRefreshTokenFromCookie(request);
                if (refreshToken != null && tokenProvider.validToken(refreshToken)) {
                    String newAccess = tokenProvider.createNewAccessTokenFromRefresh(refreshToken, accessTokenTtl);
                    Authentication auth = tokenProvider.getAuthentication(newAccess);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    // (선택) 새 액세스 토큰을 헤더로 내려줌
                    response.setHeader("X-New-Access-Token", newAccess);
                } else {
                    forceLogoutWith401(response);
                    return;
                }
            }
        } catch (ExpiredJwtException e) {
            // 액세스 만료 → 리프레시 시도
            String refreshToken = getRefreshTokenFromCookie(request);
            if (refreshToken != null && tokenProvider.validToken(refreshToken)) {
                String newAccess = tokenProvider.createNewAccessTokenFromRefresh(refreshToken, accessTokenTtl);
                Authentication auth = tokenProvider.getAuthentication(newAccess);
                SecurityContextHolder.getContext().setAuthentication(auth);
                response.setHeader("X-New-Access-Token", newAccess);
            } else {
                forceLogoutWith401(response);
                return;
            }
        } catch (JwtException | IllegalArgumentException e) {
            forceLogoutWith401(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(String header) {
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, REFRESH_TOKEN_COOKIE_NAME);
        return cookie != null ? cookie.getValue() : null;
    }

    private void forceLogoutWith401(HttpServletResponse res) throws IOException {
        SecurityContextHolder.clearContext();

        // refreshToken 삭제 쿠키
        String deleteCookie = REFRESH_TOKEN_COOKIE_NAME + "=; Path=" + REFRESH_TOKEN_COOKIE_PATH
                + "; HttpOnly; Max-Age=0; SameSite=Lax";
        res.addHeader(HttpHeaders.SET_COOKIE, deleteCookie);

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"Sign in again\"}");
    }
}

