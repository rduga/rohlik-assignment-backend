package cz.rohlik.assignment.backend.model.mapper;

import cz.rohlik.assignment.backend.entity.OrderItem;
import cz.rohlik.assignment.backend.model.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", expression = "java(orderItem.getId().getProductId())")
    OrderItemDto toDto(OrderItem orderItem);
}
