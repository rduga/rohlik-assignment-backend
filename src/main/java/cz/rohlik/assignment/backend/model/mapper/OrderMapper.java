package cz.rohlik.assignment.backend.model.mapper;

import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.model.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);
}
