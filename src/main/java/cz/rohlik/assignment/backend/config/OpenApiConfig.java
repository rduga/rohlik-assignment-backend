package cz.rohlik.assignment.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPI31
@OpenAPIDefinition(
    info = @Info(
        title = "Rohlik Assignment API",
        version = "1.0",
        description = "API documentation for Rohlik Assignment"
    )
)
public class OpenApiConfig {

}
