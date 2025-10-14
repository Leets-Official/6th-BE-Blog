package leets.blogapplication.config;

import io.jsonwebtoken.*;
import leets.blogapplication.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import leets.blogapplication.config.jwt.JwtProperties;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user, Duration expiredAt){
        Date now  = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now  = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("tokenType", "accessToken")
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public boolean validToken(String token)
            throws ExpiredJwtException, IllegalArgumentException {
        Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecret())
                .build()
                .parseClaimsJws(token);
        return true;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);
    }

    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String createRefreshToken(User user) {

        Date now  = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(7).toMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .claim("tokenType", "refreshToken")
                .compact();
    }
}
