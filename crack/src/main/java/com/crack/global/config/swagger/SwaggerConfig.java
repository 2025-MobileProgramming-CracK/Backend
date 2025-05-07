package com.crack.global.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearerAuth", securityScheme()))
        .info(apiInfo())
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }

  private Info apiInfo() {
    return new Info()
        .title("crack Springdoc")
        .description("Springdoc을 사용한 crack Swagger UI")
        .version("1.0.0");
  }

  private SecurityScheme securityScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
  }
}
