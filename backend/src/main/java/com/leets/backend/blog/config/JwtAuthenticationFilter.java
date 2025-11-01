package com.leets.backend.blog.config;

import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.UserNotFoundException;
import com.leets.backend.blog.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwt, UserRepository userRepository) {
        this.jwt = jwt; this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Long userId = jwt.getUserId(token);
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user.getUserId(), null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException e) {
                // 유효하지 않은 토큰은 무시하고 익명으로 진행(보호 리소스에서 401 처리)
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getServletPath();
        return p.startsWith("/auth"); // /auth/** 는 인증필터 스킵
    }
}
