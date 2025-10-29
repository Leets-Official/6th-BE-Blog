package leets.blogapplication.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class JwtFactory {

    private String subject = "seungjub270@gmail.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(3).toMillis());
    private Map<String, Object> claims = emptyMap();

    // 기본 생성자 (기본값 유지)
    public JwtFactory() { }

    // 전체 인자 생성자 (null이면 기본값 사용)
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.subject   = (subject   != null) ? subject   : this.subject;
        this.issuedAt  = (issuedAt  != null) ? issuedAt  : this.issuedAt;
        this.expiration= (expiration!= null) ? expiration: this.expiration;
        this.claims    = (claims    != null) ? claims    : this.claims;
    }

    // 수동 빌더
    public static class Builder {
        private String subject;
        private Date issuedAt;
        private Date expiration;
        private Map<String, Object> claims;

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }
        public Builder issuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }
        public Builder expiration(Date expiration) {
            this.expiration = expiration;
            return this;
        }
        public Builder claims(Map<String, Object> claims) {
            this.claims = claims;
            return this;
        }
        public JwtFactory build() {
            return new JwtFactory(subject, issuedAt, expiration, claims);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static JwtFactory withDefaultValues() {
        return builder().build();
    }

    public String createToken(JwtProperties jwtProperties) {
        // claims가 비어있지 않으면 방어적 복사
        Map<String, Object> safeClaims =
                (claims == null || claims.isEmpty()) ? Collections.emptyMap() : Map.copyOf(claims);

        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(safeClaims)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    // 필요하다면 게터 직접 정의 (롬복 @Getter 대체)
    public String getSubject() { return subject; }
    public Date getIssuedAt() { return issuedAt; }
    public Date getExpiration() { return expiration; }
    public Map<String, Object> getClaims() { return claims; }
}
