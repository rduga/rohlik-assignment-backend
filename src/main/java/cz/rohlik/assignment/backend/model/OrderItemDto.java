package cz.rohlik.assignment.backend.model;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class OrderItemDto {
    Long productId;

    @Positive
    BigDecimal quantity;
}
