package cz.rohlik.assignment.backend.model.mapper;

import cz.rohlik.assignment.backend.entity.Product;
import cz.rohlik.assignment.backend.model.ProductRequestDto;
import cz.rohlik.assignment.backend.model.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toDto(Product product);

    Product toEntity(ProductRequestDto productRequestDto);

    void updateEntityFromDto(ProductRequestDto productDto, @MappingTarget Product product);
}
