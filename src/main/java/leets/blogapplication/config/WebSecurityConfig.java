package leets.blogapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import leets.blogapplication.service.AccountDetailService;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
public class WebSecurityConfig {
    private final AccountDetailService accountDetailService;

    public WebSecurityConfig(AccountDetailService accountDetailService) {
        this.accountDetailService = accountDetailService;
    }
    /*.and(), .authorizeRequest() 등의 Deprecated 버전 호환 문제 해결하기*/

//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**", "/webjars/**");
//    } //H2 console에 대한 설정, 지금은 사용하지 않기에 주석처리하지만 정적리소스에 대한 permit 정보를 알기 위해 놔둠

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // REST/JWT 기반 가정
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        // 1) logout만 인증 필요
                        .requestMatchers(HttpMethod.GET, "/auth/logout").authenticated()

                        // 2) AuthController의 나머지 엔드포인트는 모두 공개
                        //.requestMatchers(HttpMethod.GET, "/posts", "/posts/*").permitAll()
                                .anyRequest().permitAll()
                        // 3) 그 외는 인증 필요
                        //.anyRequest().authenticated()
                )

                // H2 콘솔 프레임 허용
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder passwordEncoder
            ,AccountDetailService accountDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(accountDetailService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

