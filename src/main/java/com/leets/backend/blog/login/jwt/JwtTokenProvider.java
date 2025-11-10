package com.leets.backend.blog.login.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    private final Key key;

    // application.yml 에서 주입받을 값들
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessTokenValidityInMs;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenValidityInMs;

    public JwtTokenProvider(UserDetailsService userDetailsService,
                            @Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-expiration}") long accessTokenValidityInMs,
                            @Value("${jwt.refresh-expiration}") long refreshTokenValidityInMs) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Access Token
    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("❌ 토큰 만료됨");
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ 유효하지 않은 토큰");
        }
        return false;
    }

    // 인증 객체 반환
    public Authentication getAuthentication(String token) {
        String email = getEmailFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Authorization 헤더에서 토큰 추출
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
