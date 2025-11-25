package com.leets.backend.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        // Security Scheme 이름 정의
        String securitySchemeName = "Bearer Authentication";

        // 모든 API에 이 Security Requirement를 적용
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        // JWT Bearer Token을 위한 Security Scheme 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)      // HTTP 인증 방식
                .scheme("bearer")                     // Bearer 토큰 사용
                .bearerFormat("JWT")                  // JWT 형식
                .description("JWT 토큰을 입력하세요. 'Bearer ' 접두사는 자동으로 추가됩니다.");

        return new OpenAPI()
                .info(new Info()
                        .title("Blog Management API")
                        .description("블로그 관리 시스템 API 문서")
                        .version("1.0.0"))
                .addSecurityItem(securityRequirement)  // 모든 API에 보안 적용
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, securityScheme));
    }
}
