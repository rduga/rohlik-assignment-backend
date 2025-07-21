package cz.rohlik.assignment.backend.model;

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
    List<OrderItemDto> items;
}
