package com.example.INFO.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // Security 설정 추가 (Bearer 토큰)
        Components components = new Components()
                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .components(components)
                .security(List.of(securityRequirement)) // Security 설정 적용
                .servers(List.of( // 서버 URL 설정
                        new Server().url("http://localhost:8080").description("Local 서버"),
                        new Server().url("https://api.example.com").description("Production 서버")
                ))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("CodeArena Swagger")
                .description("CodeArena 유저 및 인증, PS, 알림에 관한 REST API")
                .version("1.0.0");
    }
}
