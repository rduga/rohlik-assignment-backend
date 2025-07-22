# Rohlik Assignment - Radovan Duga

## Overview

This project is a backend solution for the Rohlik assignment, built using **Java** and **Spring Boot**. It provides RESTful APIs for managing the application's core functionalities. The project uses **Maven** for dependency management and build automation.

### Key Features
- RESTful API endpoints for core operations.
- **Swagger UI** for API documentation and testing.
- **H2 Database** for in-memory data storage during development.
- Pagination and sorting support for API responses.
- Follows clean architecture principles with a focus on modularity and scalability.

### Technologies Used
- **Java**: Primary programming language.
- **Spring Boot**: Framework for building the backend application.
- **Maven**: Build and dependency management tool.
- **H2 Database**: Lightweight in-memory database for development and testing.
- **Swagger**: API documentation and testing tool.

---

## Running the Backend

You can easily start the Spring Boot backend using the provided scripts:

- **On macOS/Linux:**
  ```sh
  ./run-backend.sh
  ```
- **On Windows:**
  Double-click or run:
  ```bat
  run-backend.bat
  ```

Alternatively, you can use the Maven Wrapper directly:

- **macOS/Linux:**
  ```sh
  ./mvnw spring-boot:run
  ```
- **Windows:**
  ```bat
  mvnw.cmd spring-boot:run
  ```

The backend will start and be accessible at the configured endpoints.

---

## Architecture

The application follows a layered architecture to ensure separation of concerns and maintainability:

1. **Controller Layer**:
    - Handles incoming HTTP requests.
    - Maps requests to appropriate service methods.
    - Returns responses in a RESTful format.
    - Example: `@RestController` annotated classes.

2. **Service Layer**:
    - Contains business logic.
    - Acts as a bridge between the controller and repository layers.
    - Example: Classes annotated with `@Service`.

3. **Repository Layer**:
    - Handles data persistence and retrieval.
    - Uses Spring Data JPA for database interactions.
    - Example: Interfaces extending `JpaRepository`.

4. **Model Layer**:
    - Defines the application's core entities.
    - Example: Classes annotated with `@Entity`.

5. **Configuration Layer**:
    - Contains configuration files for database, security, and other application settings.
    - Example: `application.yml`, `application-local.yml`.

---

## Technical Documentation

### Architecture Overview
The application is designed with a layered architecture to promote modularity, scalability, and maintainability. Each layer has a specific responsibility, ensuring a clear separation of concerns. This approach simplifies testing and future enhancements.

### Trade-offs
- **H2 Database**: Chosen for development and testing due to its lightweight nature. However, it is not suitable for production environments, where a robust database like PostgreSQL or MySQL would be more appropriate.
- **Spring Boot**: Provides rapid development capabilities but may introduce overhead for smaller applications.
- **Swagger**: Simplifies API documentation but requires additional configuration for advanced features like `Pageable` support.

### Improvements
- Replace the **H2 Database** with a production-grade database for deployment.
- Implement caching mechanisms (e.g., Caffeine or Redis) to improve performance for frequently accessed data.
- Add comprehensive unit and integration tests to ensure code quality and reliability.
- Enhance security by integrating OAuth2 or JWT for authentication and authorization.
- Optimize API responses by implementing data compression and efficient serialization.

---

## API Documentation

- **Swagger UI**: Accessible at [http://localhost/rohlik-assignment-backend/swagger-ui.html](http://localhost/rohlik-assignment-backend/swagger-ui.html).
- **H2 Console**: Accessible at [http://localhost/rohlik-assignment-backend/h2-console/](http://localhost/rohlik-assignment-backend/h2-console/).

---

## References

- Problem with `Pageable.sort` in Swagger UI - use `@ParameterObject`: [StackOverflow Link](https://stackoverflow.com/questions/35404329/swagger-documentation-for-spring-pageable-interface)