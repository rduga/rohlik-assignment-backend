package cz.rohlik.assignment.backend.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class OrderDto {

    List<OrderItemDto> items;
}
