package com.example.FitnessTacker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI CustomAPI() {
        return new OpenAPI()
              .info(new Info()
                      .title("Fitness Tracking API")
                      .version("v1.O")
                      .description("Production Grade API")
                      .contact(new Contact()
                              .name("Sudhanshu Chauhan")
                              .url("https://sudhanshu.com")
                              .email("sudhanshuchauhan6789@gmail.com")
                      )
              );
    }
}