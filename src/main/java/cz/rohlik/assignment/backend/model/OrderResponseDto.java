package cz.rohlik.assignment.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Value
@Builder
@Jacksonized
public class OrderResponseDto {

    @Schema(description = "Order ID", example = "1001")
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @Schema(description = "Order creation timestamp (epoch milliseconds)", example = "1681234567890")
    Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @Schema(description = "Order last update timestamp (epoch milliseconds)", example = "1681234567999")
    Instant updatedAt;

    @Schema(description = "List of items in the order")
    List<OrderItemDto> items;

    @Schema(description = "Current status of the order", example = "PAID")
    OrderStatus status;

    @Schema(description = "Total price of the order", example = "199.99")
    BigDecimal totalPrice;
}
