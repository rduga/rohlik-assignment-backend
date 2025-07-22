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

/**
 * Service class for managing products and related business logic.
 * <p>
 * Handles product creation, retrieval, update, and deletion.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductService {

    /**
     * Repository for accessing product data.
     */
    ProductRepository productRepository;
    /**
     * Mapper for converting between Product entities and DTOs.
     */
    ProductMapper productMapper;
    /**
     * Service for managing order items related to products.
     */
    OrderItemService orderItemService;

    /**
     * Retrieves all products with pagination support.
     *
     * @param pageable pagination and sorting information
     * @return paginated list of product response DTOs
     */
    public Page<ProductResponseDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the product ID
     * @return the product response DTO
     * @throws EntityNotFoundException if the product is not found
     */
    public ProductResponseDto getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    /**
     * Creates a new product with the provided details.
     *
     * @param productRequestDto the product request DTO
     * @return the created product response DTO
     */
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toEntity(productRequestDto);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    /**
     * Updates an existing product by ID with the provided details.
     *
     * @param id the product ID
     * @param productRequestDto the product request DTO
     * @return the updated product response DTO
     * @throws EntityNotFoundException if the product is not found
     */
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        productMapper.updateEntityFromDto(productRequestDto, existingProduct);
        Product savedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(savedProduct);
    }

    /**
     * Deletes a product by its ID if it is not associated with any order items.
     *
     * @param id the product ID
     * @throws EntityNotFoundException if the product is not found
     * @throws ProductCanotBeDeletedException if the product is associated with order items
     */
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
