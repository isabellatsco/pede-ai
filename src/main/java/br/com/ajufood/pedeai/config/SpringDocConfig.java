package br.com.ajufood.pedeai.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8080/pedeai/swagger-ui/index.html

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {]
        return new OpenAPI()
                .info(new Info()
                        .title("API - Projeto PedeAI")
                        .contact(new Contact()
                                .name("Equipe PedeAI")
                                .email("faleconosco@pedeai.com.br")
                                .url("pedeai.com.br"))
                        .description("Projeto PedeAI")
                        .version("v0.0.1"))
                        
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação")
                        .url("https://www.pedeai.com.br/docs/open-api"));
    }
