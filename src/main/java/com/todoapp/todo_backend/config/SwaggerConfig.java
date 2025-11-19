package com.todoapp.todo_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures OpenAPI/Swagger documentation for the application.
 *
 * <p>This configuration class customizes metadata for automatically generated
 * API documentation, including title, version, and description. Springdoc
 * integrates with Spring Web to expose interactive API docs via Swagger UI.</p>
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI todoServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo API")
                        .description("API for To do list.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Uditha")
                                .email("udithanayanajith97@gmail.com")));
    }
}
