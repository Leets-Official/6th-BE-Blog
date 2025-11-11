package leets.blogapplication.config;

import leets.blogapplication.handler.KakaoLoginSuccessHandler;
import leets.blogapplication.service.auth.kakao.KakaoOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    //spring security의 정책, 필터 설정

    private final TokenProvider tokenProvider;
    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    private final KakaoLoginSuccessHandler kakaoLoginSuccessHandler;

    public WebSecurityConfig(TokenProvider tokenProvider, KakaoOAuth2UserService kakaoOAuth2UserService, KakaoLoginSuccessHandler kakaoLoginSuccessHandler) {
        this.tokenProvider = tokenProvider;
        this.kakaoOAuth2UserService = kakaoOAuth2UserService;
        this.kakaoLoginSuccessHandler = kakaoLoginSuccessHandler;
    }

    // 체인 1: OAuth2 로그인 전용(세션 필요)
    @Bean @Order(1)
    SecurityFilterChain oauth2Chain(HttpSecurity http) throws Exception {
        http.securityMatcher("/oauth2/**", "/login/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // ❌ 여기엔 토큰필터 넣지 말 것
                .oauth2Login(o -> o
                        .userInfoEndpoint(u -> u.userService(kakaoOAuth2UserService))
                        .successHandler(kakaoLoginSuccessHandler)     // or .defaultSuccessUrl("/oauth/signed-in", true)
                        .failureUrl("/login?error")                   // 에러 확인용
                );
        return http.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/signup", "/oauth2/**","/login/**","/error").permitAll()
                        .requestMatchers("/auth/logout", "/comments/**", "/post/**", "/posts/**").authenticated()
                        .anyRequest().authenticated() // 나머지 보호하려면 이렇게. 전부 공개면 permitAll
                )

                .headers(h -> h.frameOptions(f -> f.sameOrigin()))

                // SecurityContextHolder에서 유저 정보 빼오기 위해서 필수 add 코드
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


