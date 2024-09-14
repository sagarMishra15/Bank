package com.app.Bank.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



//@io.swagger.v3.oas.annotations.security.SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
@OpenAPIDefinition(info = @Info(title = "MyBankAPIs", version = "v1", description = "This document helps developer to read/test all Bank APIs."),
        servers = {
                @Server(description = "local server", url = "http://localhost:8080/"),
                @Server(description = "IP server", url = "https://192.168.1.10:8080/"),
        },
        security = {
                @SecurityRequirement(
                        name = "bearer-key",
                        scopes = {}
                ),
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components()
                .addSecuritySchemes("bearer-key",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT"))
        );
    }
}
