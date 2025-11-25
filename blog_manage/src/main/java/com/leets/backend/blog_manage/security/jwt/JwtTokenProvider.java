package com.leets.backend.blog_manage.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증 처리
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";

    private final Key key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationTime) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    /** Access Token 생성 */
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date expiryDate = new Date(now + accessTokenExpirationTime);

        return Jwts.builder()
                .setSubject(authentication.getName()) // User의 이메일
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /** Refresh Token 생성 */
    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date expiryDate = new Date(now + refreshTokenExpirationTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /** 토큰에서 Authentication 객체 생성 */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 반환
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.warn("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /** 토큰에서 Claims 추출 */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /** 토큰에서 이메일(Subject) 추출 */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /** Request Header에서 토큰 추출 */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}