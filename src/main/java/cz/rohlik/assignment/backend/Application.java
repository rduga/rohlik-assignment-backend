package cz.rohlik.assignment.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Rohlik Assignment Spring Boot application.
 * <p>
 * This class bootstraps the application and enables scheduling for background tasks.
 */
@SpringBootApplication
@EnableScheduling
public class Application {
    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
