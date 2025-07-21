package cz.rohlik.assignment.backend.service;

import cz.rohlik.assignment.backend.entity.Product;
import cz.rohlik.assignment.backend.exception.ProductCanotBeDeletedException;
import cz.rohlik.assignment.backend.model.ProductRequestDto;
import cz.rohlik.assignment.backend.model.ProductResponseDto;
import cz.rohlik.assignment.backend.model.mapper.ProductMapper;
import cz.rohlik.assignment.backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;
    OrderItemService orderItemService;

    public Page<ProductResponseDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    public ProductResponseDto getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toEntity(productRequestDto);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        productMapper.updateEntityFromDto(productRequestDto, existingProduct);
        Product savedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }

        if (orderItemService.orderItemExistsByProductId(id)) {
            throw new ProductCanotBeDeletedException("Cannot delete product with existing order items");
        }

        productRepository.deleteById(id);
    }
}
