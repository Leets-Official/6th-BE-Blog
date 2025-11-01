package com.leets.backend.blog.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessExpMin;
    private final long refreshExpDays;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-exp-min}") long accessExpMin,
            @Value("${app.jwt.refresh-exp-days}") long refreshExpDays
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMin = accessExpMin;
        this.refreshExpDays = refreshExpDays;
    }

    public String generateAccessToken(Long userId, String email, String role){
        Instant now = Instant.now();
        Instant exp = now.plus(accessExpMin, ChronoUnit.MINUTES);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId){
        Instant now = Instant.now();
        Instant exp = now.plus(refreshExpDays, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Long getUserId(String token){
        return Long.valueOf(parse(token).getBody().getSubject());
    }

    public boolean isExpired(String token){
        return parse(token).getBody().getExpiration().before(new Date());
    }
}
