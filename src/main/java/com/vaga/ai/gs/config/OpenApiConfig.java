package com.vaga.ai.gs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Configuration
public class OpenApiConfig {

    // BLOCO ESTÁTICO: Executa uma vez ao carregar a classe
    static {
        // Diz ao SpringDoc para ignorar completamente parâmetros com essa anotação
        SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthenticationPrincipal.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Vaga AI")
                        .version("1.0")
                        .description("API para gerenciamento de vagas e currículos com Inteligência Artificial")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                // Configuração para o botão "Authorize" funcionar com JWT no Swagger
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}