package cz.rohlik.assignment.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow CORS for all paths
                    .allowedOriginPatterns("http://localhost:[*]") // Allow all origins
                    .allowedMethods("*") // Allow all HTTP methods
                    .allowedHeaders("*") // Allow all headers
                    .allowCredentials(true);
            }
        };
    }
}