package cz.rohlik.assignment.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class OrderItemDto {
    @Schema(description = "ID of the product being ordered", example = "1")
    Long productId;

    @Positive
    @Schema(description = "Quantity of the product ordered", example = "2.5")
    BigDecimal quantity;
}
