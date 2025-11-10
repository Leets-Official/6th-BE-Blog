package com.leets.backend.blog.config.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager; // 1. RefreshToken 임포트
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // 2. User 임포트
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 3. RefreshTokenRepository 임포트

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.backend.blog.domain.RefreshToken;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.dto.TokenInfo;
import com.leets.backend.blog.dto.UserLoginRequestDto;
import com.leets.backend.blog.repository.RefreshTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * POST /login 요청을 처리하고, 인증 성공 시 JWT 토큰을 발급하는 필터
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository; // 4. Repository 주입
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtLoginFilter(AuthenticationManager authenticationManager, 
                            JwtTokenProvider jwtTokenProvider,
                            RefreshTokenRepository refreshTokenRepository) { // 5. 생성자에 추가
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository; // 6. 초기화
        setFilterProcessesUrl("/login");
    }

    /**
     * 1. 로그인 시도 (Authentication)
     * (기존 코드와 동일)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserLoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getEmail(),
                    loginRequestDto.getPassword(),
                    null
            );

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 처리 중 오류 발생", e);
        }
    }

    /**
     * 2. 인증 성공 (Successful Authentication)
     * (DB 저장 로직 추가됨)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        // 1. authResult (인증 결과)를 기반으로 JWT 토큰(Access, Refresh) 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateTokenInfo(authResult);

        // 2. ========== Refresh Token DB에 저장 ==========
        // authResult.getPrincipal()은 CustomUserDetailsService에서 반환한 UserDetails(User 객체)입니다.
        User user = (User) authResult.getPrincipal();
        Long userId = user.getId();
        
        // RefreshToken 엔티티 생성
        RefreshToken refreshToken = new RefreshToken(userId, tokenInfo.getRefreshToken());
        
        // DB에 저장 (userId가 PK이므로, 이미 존재하면 UPDATE, 없으면 INSERT)
        refreshTokenRepository.save(refreshToken);
        // ===============================================

        // 3. Response Body에 TokenInfo(JSON)를 담아 클라이언트에 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenInfo));
    }

    /**
     * 3. 인증 실패 (Unsuccessful Authentication)
     * (기존 코드와 동일)
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException failed) throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"로그인 실패\", \"message\": \"" + failed.getMessage() + "\"}");
    }
}

