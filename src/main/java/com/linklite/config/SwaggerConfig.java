package com.linklite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI linkLiteOpenAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("LinkLite URL Shortener API")

                        .version("1.0")

                        .description("REST API for LinkLite URL Shortener")

                        .contact(new Contact()

                                .name("Alekhya")

                                .email("alekhya@example.com")

                                .url("https://github.com/alekhyaGontla")));
    }
}