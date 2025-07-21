package cz.rohlik.assignment.backend.exception;

import jakarta.validation.ValidationException;

public class OrderCannotBeCancelledException extends ValidationException {
    public OrderCannotBeCancelledException(String message) {
        super(message);
    }
}
