package com.leets.backend.blog.config;

import com.leets.backend.blog.config.jwt.JwtAuthenticationFilter;
import com.leets.backend.blog.config.jwt.JwtLoginFilter;
import com.leets.backend.blog.config.jwt.JwtTokenProvider;
import com.leets.backend.blog.repository.RefreshTokenRepository;
import com.leets.backend.blog.service.CustomOAuth2UserService;
import com.leets.backend.blog.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.leets.backend.blog.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// 1. [삭제] WebSecurityCustomizer Import 제거
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
    private final UserRepository userRepository;

    private static final String[] SWAGGER_PATHS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    // (생성자는 동일)
    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          AuthenticationConfiguration authenticationConfiguration,
                          RefreshTokenRepository refreshTokenRepository,
                          CustomOAuth2UserService customOAuth2UserService,
                          UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConfiguration = authenticationConfiguration;
        this.refreshTokenRepository = refreshTokenRepository;
        this.customOAuth2UserService = customOAuth2UserService;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 2. [삭제] WebSecurityCustomizer Bean 제거
    /*
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers(SWAGGER_PATHS)
                .requestMatchers("/error");
    }
    */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
                authenticationManager,
                jwtTokenProvider,
                refreshTokenRepository
        );
        jwtLoginFilter.setFilterProcessesUrl("/login");

        OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler =
                new OAuth2AuthenticationSuccessHandler(jwtTokenProvider, refreshTokenRepository, userRepository);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureUrl("/loginFailure")
                )

                // 3. [수정] authorizeHttpRequests를 한 곳에서 관리
                .authorizeHttpRequests(authz -> authz
                        // 3-1. (추가) 기존 WebSecurityCustomizer에 있던 경로들
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .requestMatchers("/error").permitAll()

                        // 3-2. (기존) OAuth2 및 JWT 관련 경로들
                        .requestMatchers("/auth/**", "/login").permitAll()
                        .requestMatchers("/login/oauth2/code/**").permitAll()

                        // 3-3. (기존) GET 요청 허용 경로
                        .requestMatchers(HttpMethod.GET, "/posts", "/posts/**").permitAll()

                        // 3-4. (기존) 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}