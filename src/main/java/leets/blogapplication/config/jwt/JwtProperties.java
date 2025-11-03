package leets.blogapplication.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    //jwt 환경설정 값 바인딩 (yml, 환경변수에서 불러옴)
    private String issuer;
    private String secret;

    public String getIssuer() { return issuer; }
    public String getSecret() { return secret; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public void setSecret(String secret) { this.secret = secret; }
}
