package cz.rohlik.assignment.backend.exception;

import jakarta.validation.ValidationException;

public class ProductCanotBeDeletedException extends ValidationException {
    public ProductCanotBeDeletedException(String message) {
        super(message);
    }
}
