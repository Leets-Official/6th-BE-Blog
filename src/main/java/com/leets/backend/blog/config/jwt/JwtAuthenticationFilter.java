package com.leets.backend.blog.config.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 클라이언트 요청 시 JWT 인증을 수행하는 필터
 * OncePerRequestFilter: 모든 서블릿 요청에 대해 단 한 번만 실행되도록 보장
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    // JwtTokenProvider를 주입받음
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 실제 필터링 로직
     * 토큰을 검사하여 유효하면 SecurityContext에 인증 정보를 저장
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header에서 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰이 유효할 경우, 토큰에서 Authentication 객체 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            
            // 4. SecurityContext에 Authentication 객체 저장
            // (이로써 Spring Security가 이 사용자를 '인증된 사용자'로 인식함)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 필터 체인으로 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 "Authorization" 헤더를 찾아 토큰(Bearer 접두사 제거)을 추출
     * @param request
     * @return 추출된 토큰 (없으면 null)
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " (7글자) 이후의 토큰 반환
        }
        return null;
    }
}
