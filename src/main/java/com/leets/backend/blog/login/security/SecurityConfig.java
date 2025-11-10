package com.leets.backend.blog.login.security;

import com.leets.backend.blog.login.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CustomUserDetailsService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (Swagger 요청 403 방지)
                .csrf(AbstractHttpConfigurer::disable)
                // 요청별 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/webjars/**"
                        ).permitAll()

                        // 로그인, 회원가입 허용
                        .requestMatchers("/auth/**", "/login/**", "/users/**").permitAll()

                        // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )

                // 세션 사용하지 않도록 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 로그인 폼 비활성화 -> AuthController에서 로그인 로직 구현 예정
                .formLogin(AbstractHttpConfigurer::disable)

                // JWT 기반 인증이기에 기본 브라우저 팝업 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // JWT 인증 필터
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    // 비밀번호 암호화 하기 위한 PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 실제 로그인 처리 담당하는 AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

