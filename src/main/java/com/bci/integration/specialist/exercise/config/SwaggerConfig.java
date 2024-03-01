package com.bci.integration.specialist.exercise.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = { @Server(url = "/") })
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI swagger() {
    return new OpenAPI()
        .info(new Info().title("Ejercicio de especialista en integracion")
            .description("API Documentation")
            .version("1.0")
            .contact(new Contact().name("Ezequiel").email("ezequiel.bustamante@globallogic.com"))
            .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }

}
