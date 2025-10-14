package leets.blogapplication.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;
    private String secret;

    public String getIssuer() { return issuer; }
    public String getSecret() { return secret; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public void setSecret(String secret) { this.secret = secret; }

    public JwtProperties() { }
    public JwtProperties(String issuer, String secret) {
        this.issuer = issuer;
        this.secret = secret;
    }
}
