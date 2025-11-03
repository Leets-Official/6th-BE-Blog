package leets.blogapplication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import leets.blogapplication.config.TokenProvider;
import leets.blogapplication.dto.req.LogInWithEmail;
import leets.blogapplication.dto.req.SignUpWithEmail;
import leets.blogapplication.service.AuthService;
import leets.blogapplication.service.auth.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationNotSupportedException;
import java.time.Duration;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    public AuthController(TokenService tokenService, AuthService authService, TokenProvider tokenProvider) {
        this.tokenService = tokenService;
        this.authService = authService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signupWithEmail(@RequestBody SignUpWithEmail req){
        try {
            authService.signUpWithEmail(req);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> loginWithEmail(@RequestBody LogInWithEmail req){
        try {
            authService.logInWithEmail(req);

            String ref = tokenService.createNewRefreshToken(req.getEmail());
            String acs = tokenService.createNewAccessToken(ref);

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", ref)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Strict")
                    .path("/") //해당 설정은 쿠키에 대한 설정, WebSecurityConfig와는 완전히 다른 설정임
                    .maxAge(Duration.ofDays(1))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + acs)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<Void> logoutWithEmail(HttpServletRequest req){
        String ref = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refresh_token".equals(c.getName())) {
                    ref = c.getValue();
                    break;
                }
            }
        }

        if (ref != null && !ref.isBlank()) {
            authService.logoutWithEmail(ref);
        }

        ResponseCookie delete = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, delete.toString())
                .build();
    }
}
