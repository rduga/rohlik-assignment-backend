package cz.rohlik.assignment.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
@Jacksonized
public class ProductResponseDto {

    Long id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant updatedAt;

    String name;

    BigDecimal pricePerUnit;

    BigDecimal stockQuantity;
}
