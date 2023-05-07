package uz.weather.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Weather service",
                description = "Project for getting weather info of cities",
                contact = @Contact(name = "Dovud Tulkinjonov",
                        email = "foundation240897@gmail.com",
                        url = "https://t.me/ibnNemat"
                ),
                version = "1.0"
        )
)
public class SwaggerConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        final String securitySchemeName = "Authorization";
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8787")
                                .description("Production")
                ))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));


    }
}
