package com.leets.backend.blog.config;

import org.springframework.context.annotation.Bean; // 1. import 추가
import org.springframework.context.annotation.Configuration; // 2. import 추가
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leets.backend.blog.config.jwt.JwtAuthenticationFilter;
import com.leets.backend.blog.config.jwt.JwtTokenProvider; // 3. import 추가

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 4. JwtTokenProvider 주입을 위한 필드 선언
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] SWAGGER_PATHS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    // 5. 생성자를 통해 JwtTokenProvider 주입
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers(SWAGGER_PATHS)
                .requestMatchers("/error"); 
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                    
                    // 6. [중요] URL 권한 설정 수정
                    // 과제 요구사항: 회원가입(/auth), 로그인(/login)은 인증 없이 접근 가능해야 함
                    .requestMatchers("/auth/**", "/login").permitAll() 
                    .requestMatchers(HttpMethod.GET, "/posts", "/posts/**").permitAll()
                    
                    // 7. [중요] 위에서 정의한 경로 외의 모든 요청은 인증(로그인)이 필요함
                    .anyRequest().authenticated() 
            )
            // 8. [핵심] JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
            // (UsernamePasswordAuthenticationFilter: Spring Security의 기본 로그인 필터)
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), 
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
