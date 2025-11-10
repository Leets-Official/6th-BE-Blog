package com.leets.backend.blog.config.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors; // 2. Logger import 추가

import org.slf4j.Logger; // 3. LoggerFactory import 추가
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.leets.backend.blog.dto.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// @Slf4j // 4. Lombok 어노테이션 제거
@Component
public class JwtTokenProvider {

    // 5. 표준 SLF4J 로거 선언
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String GRANT_TYPE = "Bearer";

    private final Key key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    // application.properties에서 비밀키와 만료 시간을 가져옵니다.
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
        
        // 6. BASE6S64 -> BASE64 로 오타 수정
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); 
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    /**
     * 1. 로그인 성공 후 호출: Access Token과 Refresh Token을 생성합니다.
     * @param authentication Spring Security의 인증 정보
     * @return TokenInfo (GrantType, AccessToken, RefreshToken)
     */
    public TokenInfo generateTokenInfo(Authentication authentication) {
        // 1. 인증 정보에서 권한 목록 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        
        // 2. Access Token 생성
        String accessToken = createAccessToken(authentication.getName(), authorities, now);

        // 3. Refresh Token 생성
        String refreshToken = createRefreshToken(now);

        // 4. TokenInfo DTO에 담아 반환
        return TokenInfo.of(GRANT_TYPE, accessToken, refreshToken);
    }

    /**
     * 2. Access Token 생성 메서드
     */
    public String createAccessToken(String subject, String authorities, long now) {
        Date accessTokenExpiresIn = new Date(now + this.accessTokenExpirationMs);
        
        return Jwts.builder()
                .setSubject(subject) // 사용자 식별자 (우리는 email 사용)
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보
                .setExpiration(accessTokenExpiresIn) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * 3. Refresh Token 생성 메서드
     * (Refresh Token은 사용자 정보 없이 만료 시간만 가짐)
     */
    public String createRefreshToken(long now) {
        Date refreshTokenExpiresIn = new Date(now + this.refreshTokenExpirationMs);

        return Jwts.builder()
                .setExpiration(refreshTokenExpiresIn) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * 4. 토큰 복호화: 토큰에서 인증 정보(Authentication)를 추출합니다.
     * (이 메서드는 JwtAuthenticationFilter에서 사용됩니다)
     * @param accessToken 검증할 Access Token
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String accessToken) {
        // 1. 토큰에서 Claims (정보 단위) 추출
        Claims claims = getClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 2. Claims에서 권한 정보(authorities) 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 3. UserDetails 객체 생성 (principal: 사용자 식별자, 여기서는 email)
        // User는 Spring Security가 제공하는 UserDetails 구현체입니다.
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        // 4. Authentication 객체(UsernamePasswordAuthenticationToken) 반환
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 5. 토큰 유효성 검증 메서드
     * (이 메서드는 JwtAuthenticationFilter에서 사용됩니다)
     * @param token 검증할 토큰
     * @return 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.", e);
            // TODO: Access Token 만료 시 Refresh Token을 사용한 재발급 로직 필요
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    /**
     * (Helper) 토큰에서 Claims를 추출합니다.
     */
    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이라도 Claims는 반환 (재발급 로직 등에서 사용 가능)
            return e.getClaims();
        }
    }
}

