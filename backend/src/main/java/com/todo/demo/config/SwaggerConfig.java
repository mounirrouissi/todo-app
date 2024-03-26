package com.todo.demo.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition()
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerApiConfig() {
        var info = new Info()
                .title("TO DO LIST API 3")
                .description("Spring Boot Project to demonstrate SwaggerUI integration")
                .version("1.0");

        return new OpenAPI().info(info);
    }
}