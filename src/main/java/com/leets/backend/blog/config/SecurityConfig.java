package com.leets.backend.blog.config;

import com.leets.backend.blog.config.jwt.JwtAuthenticationFilter;
import com.leets.backend.blog.config.jwt.JwtLoginFilter;
import com.leets.backend.blog.config.jwt.JwtTokenProvider;
import com.leets.backend.blog.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 1. [수정] 직접 생성하지 않고 주입받기 위해 필드 추가
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    // UserRepository는 이제 SecurityConfig에서 쓰지 않으므로 제거했습니다.

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          AuthenticationConfiguration authenticationConfiguration,
                          RefreshTokenRepository refreshTokenRepository,
                          CustomOAuth2UserService customOAuth2UserService,
                          // 2. [수정] 생성자 주입
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConfiguration = authenticationConfiguration;
        this.refreshTokenRepository = refreshTokenRepository;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
                authenticationManager,
                jwtTokenProvider,
                refreshTokenRepository
        );
        jwtLoginFilter.setFilterProcessesUrl("/login");

        // 3. [삭제] 여기서 new로 직접 생성하던 코드를 삭제함
        // OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler = ... (삭제)

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        // 4. [수정] 주입받은 핸들러 사용
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureUrl("/loginFailure")
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/auth/**", "/login").permitAll()
                        .requestMatchers("/login/oauth2/code/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts", "/posts/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}