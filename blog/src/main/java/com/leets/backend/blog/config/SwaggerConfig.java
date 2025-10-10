package com.leets.backend.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Leets 6th Blog API Documentation")
                        .description("블로그 게시물 CRUD 기능에 대한 API 명세서입니다.")
                        .version("v1.0.0"));
    }
}
