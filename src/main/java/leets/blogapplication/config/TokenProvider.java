package leets.blogapplication.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import leets.blogapplication.config.jwt.JwtProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;

@Service
public class TokenProvider {
    //토큰 생성, 검증, 게터 메서드

    private final JwtProperties props;
    private final Key hmacKey;

    public TokenProvider(JwtProperties props) {
        this.props = props;
        this.hmacKey = buildKey(props.getSecret());
    }

    private Key buildKey(String secret) {
        // Base64 문자열이면 decode, 아니면 bytes
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException e) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserId(String token){
        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = claims.get("id", Long.class);
        return userId;
    }

    public String generateAccessToken(String email, Long userId, Duration ttl) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttl.toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("tokenType", "accessToken");

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(props.getIssuer())
                .setIssuedAt(now)
                .setExpiration(exp)
                .setSubject(email)
                .addClaims(claims)
                .signWith(hmacKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, Long userId, Duration ttl) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttl.toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("tokenType", "refreshToken");

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(props.getIssuer())
                .setIssuedAt(now)
                .setExpiration(exp)
                .setSubject(email)               // refresh도 sub=email (재발급에 DB 없이 사용)
                .addClaims(claims)
                .signWith(hmacKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createNewAccessTokenFromRefresh(String refreshToken, Duration accessTtl) {
        Claims claims = parseClaims(refreshToken);
        String tokenType = claims.get("tokenType", String.class);
        if (!"refreshToken".equals(tokenType)) {
            throw new JwtException("Not a refresh token");
        }
        String email = claims.getSubject();
        Number idNum = claims.get("id", Number.class);
        Long userId = (idNum != null) ? idNum.longValue() : null;
        return generateAccessToken(email, userId, accessTtl);
    }

    public boolean validToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e; // 만료는 상위에서 처리하도록 그대로 던짐
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 변조/형식오류 → false
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        // 필요한 경우 role을 클레임으로 넣어서 동적으로 구성 가능
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(), "", authorities
                );

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
