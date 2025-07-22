package cz.rohlik.assignment.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class ProductRequestDto {

    @Size(min = 1, max = 50)
    @NotEmpty
    @Schema(description = "Name of the product", example = "Milk")
    String name;

    @Positive
    @Schema(description = "Price per unit of the product", example = "19.99")
    BigDecimal pricePerUnit;

    @PositiveOrZero
    @Schema(description = "Available stock quantity", example = "100")
    BigDecimal stockQuantity;
}
