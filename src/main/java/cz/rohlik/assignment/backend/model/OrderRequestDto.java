package cz.rohlik.assignment.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class OrderRequestDto {

    @NotEmpty
    @Schema(description = "List of items included in the order")
    List<OrderItemDto> items;
}
