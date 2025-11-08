package com.leets.backend.blog_manage.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // API 기본 정보 설정
        Info info = new Info()
                .title("Leets Blog API")
                .version("v1.0.0")
                .description("블로그 프로젝트 API 명세서");

        // --- [핵심] JWT 보안 스키마 정의 ---
        String jwtSchemeName = "bearerAuth"; // SecurityScheme의 이름 (임의 지정 가능)

        // 1. 보안 요구사항 정의 (어떤 스키마를 사용할지)
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        // 2. 보안 스키마 정의 (JWT Bearer 방식)
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName) // 스키마 이름
                        .type(SecurityScheme.Type.HTTP) // 타입: HTTP
                        .scheme("bearer") // 스키마: bearer
                        .bearerFormat("JWT")); // 베어러 포맷: JWT

        // 3. OpenAPI 객체에 정보 및 보안 설정 추가
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement) // 전역 보안 요구사항 추가
                .components(components); // 컴포넌트에 보안 스키마 추가
    }
}