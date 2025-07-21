package cz.rohlik.assignment.backend.rest;

import cz.rohlik.assignment.backend.model.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * This class can be used to handle exceptions globally for all REST controllers
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler({
        ValidationException.class,
        MethodArgumentNotValidException.class,
        PropertyReferenceException.class,
        OptimisticLockException.class
    })
    public ResponseEntity<ErrorResponseDto> handle(Exception ex) {
        return getErrorResponseDtoResponseEntity(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handle(EntityNotFoundException ex) {
        return getErrorResponseDtoResponseEntity(ex, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<ErrorResponseDto> getErrorResponseDtoResponseEntity(
        Throwable throwable, HttpStatus httpStatus, String message
    ) {
        log.error("Exception {} occurred: {}", throwable.getClass().getSimpleName(), throwable.getMessage(), throwable);

        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .message(message)
                .error(httpStatus.getReasonPhrase())
                .build());
    }
}
