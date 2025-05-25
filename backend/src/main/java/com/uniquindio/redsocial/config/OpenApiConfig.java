package com.uniquindio.redsocial.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Red Social de Aprendizaje API")
                        .version("1.0")
                        .description("API para la plataforma de aprendizaje colaborativo")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu@email.com")));
    }
}