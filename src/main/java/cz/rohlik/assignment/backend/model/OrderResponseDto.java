package cz.rohlik.assignment.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Value
@Builder
@Jacksonized
public class OrderResponseDto {

    Long id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant updatedAt;

    List<OrderItemDto> items;

    OrderStatus status;

    BigDecimal totalPrice;
}
