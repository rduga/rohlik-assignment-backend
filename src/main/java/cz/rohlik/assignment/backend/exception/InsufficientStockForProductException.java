package cz.rohlik.assignment.backend.exception;

import jakarta.validation.ValidationException;

public class InsufficientStockForProductException extends ValidationException {
    public InsufficientStockForProductException(String message) {
        super(message);
    }
}
