package com.leets.backend.blog_manage.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 설정
 * API 문서화 및 JWT 인증 설정
 */
@Configuration
public class SwaggerConfig {

    /** OpenAPI 설정 - JWT Bearer 인증 스키마 포함 */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Leets Blog API")
                .version("v1.0.0")
                .description("블로그 프로젝트 API 명세서");

        String jwtSchemeName = "bearerAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}