package com.uniquindio.redsocial.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Value("${spring.application.name:Red Social de Aprendizaje}")
    private String applicationName;

    @Value("${spring.application.version:1.0}")
    private String applicationVersion;

    @Value("${spring.profiles.active:development}")
    private String activeProfile;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title(applicationName + " API")
                        .version(applicationVersion)
                        .description("API REST para la plataforma de aprendizaje colaborativo")
                        .termsOfService("https://ejemplo.com/terminos")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@redsocialaprendizaje.com")
                                .url("https://redsocialaprendizaje.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(defineServers());
    }

    private List<Server> defineServers() {
        Server developmentServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de Desarrollo");

        Server productionServer = new Server()
                .url("https://api.redsocialaprendizaje.com")
                .description("Servidor de Producción");

        return "production".equals(activeProfile) ?
                List.of(productionServer) :
                List.of(developmentServer, productionServer);
    }

    @Bean
    public Components securityComponents() {
        return new Components()
                .addParameters("tenant-id", new io.swagger.v3.oas.models.parameters.Parameter()
                        .in("header")
                        .required(true)
                        .description("ID del inquilino")
                        .schema(new io.swagger.v3.oas.models.media.StringSchema()));
    }
}