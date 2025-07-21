package cz.rohlik.assignment.backend.model.mapper;

import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.entity.OrderItem;
import cz.rohlik.assignment.backend.model.OrderItemDto;
import cz.rohlik.assignment.backend.model.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    protected OrderItemMapper orderItemMapper;

    @Mapping(target = "items", expression = "java(mapItems(order.getItems()))")
    public abstract OrderResponseDto toDto(Order order);

    protected List<OrderItemDto> mapItems(List<OrderItem> items) {
        return items.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
