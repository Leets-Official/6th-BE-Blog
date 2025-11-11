package com.leets.backend.blog.config;

import org.springframework.context.annotation.Bean; // 1. import 추가
import org.springframework.context.annotation.Configuration; // 2. import 추가

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition(
        servers = @Server(url = "http://localhost:8080")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Leets Blog REST API")
                        .description("3주차 과제 - 블로그 REST API 명세서")
                        .version("v1.0.0"));
    }
}