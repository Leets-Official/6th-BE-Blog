package com.leets.backend.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // 1. RefreshTokenRepository import 추가
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leets.backend.blog.config.jwt.JwtAuthenticationFilter;
import com.leets.backend.blog.config.jwt.JwtLoginFilter;
import com.leets.backend.blog.config.jwt.JwtTokenProvider;
import com.leets.backend.blog.repository.RefreshTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenRepository refreshTokenRepository; // 2. Repository 필드 추가

    private static final String[] SWAGGER_PATHS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    // 3. 생성자 수정 (RefreshTokenRepository 주입)
    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          AuthenticationConfiguration authenticationConfiguration,
                          RefreshTokenRepository refreshTokenRepository) { // 3. 주입 받기
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConfiguration = authenticationConfiguration;
        this.refreshTokenRepository = refreshTokenRepository; // 3. 초기화
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
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
        
        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

        // 4. [수정] JwtLoginFilter 인스턴스 생성 시 Repository 주입
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
                authenticationManager, 
                jwtTokenProvider, 
                refreshTokenRepository // 4. Repository 전달
        );
        jwtLoginFilter.setFilterProcessesUrl("/login");

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                    
                    .requestMatchers("/auth/**", "/login").permitAll() 
                    .requestMatchers(HttpMethod.GET, "/posts", "/posts/**").permitAll()
                    .anyRequest().authenticated() 
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

