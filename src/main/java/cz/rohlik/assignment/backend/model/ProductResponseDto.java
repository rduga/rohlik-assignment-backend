package cz.rohlik.assignment.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

@Value
@Builder
@Jacksonized
public class ProductResponseDto {

    @Schema(description = "Product ID", example = "2001")
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @Schema(description = "Product creation timestamp (epoch milliseconds)", example = "1681234567890")
    Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @Schema(description = "Product last update timestamp (epoch milliseconds)", example = "1681234567999")
    Instant updatedAt;

    @Schema(description = "Name of the product", example = "Milk")
    String name;

    @Schema(description = "Price per unit of the product", example = "19.99")
    BigDecimal pricePerUnit;

    @Schema(description = "Available stock quantity", example = "100")
    BigDecimal stockQuantity;
}
