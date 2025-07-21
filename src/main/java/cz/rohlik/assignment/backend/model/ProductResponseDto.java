package cz.rohlik.assignment.backend.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class ProductResponseDto {

    Long id;

    String name;

    BigDecimal pricePerUnit;

    BigDecimal stockQuantity;
}
