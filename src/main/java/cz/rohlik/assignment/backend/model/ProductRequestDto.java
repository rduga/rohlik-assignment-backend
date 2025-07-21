package cz.rohlik.assignment.backend.model;

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
    String name;

    @Positive
    BigDecimal pricePerUnit;

    @PositiveOrZero
    BigDecimal stockQuantity;
}
