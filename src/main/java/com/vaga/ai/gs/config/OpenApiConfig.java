package com.vaga.ai.gs.config;

import com.vaga.ai.gs.model.User;
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

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthenticationPrincipal.class);
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(User.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Vaga AI")
                        .version("1.0")
                        .description("API para gerenciamento de vagas e currículos com Inteligência Artificial")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}