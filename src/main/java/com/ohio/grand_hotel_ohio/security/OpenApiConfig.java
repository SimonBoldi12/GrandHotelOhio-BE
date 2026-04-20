package com.ohio.grand_hotel_ohio.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)


public class OpenApiConfig {
    @Bean
    public GroupedOpenApi grandHotelOhioApi() {
        return GroupedOpenApi.builder()
                .group("ohio-api")
                .packagesToScan(
                        "com.ohio.grand_hotel_ohio.controller"
                )
                .build();
    }

}
