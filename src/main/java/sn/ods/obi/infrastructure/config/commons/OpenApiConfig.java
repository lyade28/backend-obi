package sn.ods.obi.infrastructure.config.commons;



/*@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = " MFPAI",
                        email = "no-reply@yopmail0.com",
                        url = "https://gainde2000.sn"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - MFPAI",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "http://localhost:9080/backendmfpai-api-v1"
                      //  url = "http://localhost:8097"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:9080/backendmfpai-api-v1"
                        //url = "http://localhost:8097"
                ),
                @Server(
                        description = "DEV ENV",
                        url = "http://10.3.130.200:31655/projet-api-v2"
                      //  url = "http://localhost:8097"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://10.3.130.200:31655/projet-api-v2"
                        //  url = "http://localhost:8097"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)*/

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("MFPAI REST API")
                        .description("MFPAI Integration API.")
                        .version("1.0").contact(new Contact().name("MFPAI")
                                .email( "www.codewithbisky.com").url("no-reply@gainde2000.dn"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }

}
