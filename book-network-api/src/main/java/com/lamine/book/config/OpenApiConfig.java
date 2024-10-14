package com.lamine.book.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import jdk.jfr.Description;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Ousmane Ibrahim Mohamed",
                        email = "contact@mohamed.com"
                ),
                description = "OpenApi Documentation for Spring Security",
                title = "Open Api Specification - Mohamed",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"

        ),
        servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "https://localhost:8088/api/v1"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://mohamed.com/project/book/social"
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

)
public class OpenApiConfig {
}
