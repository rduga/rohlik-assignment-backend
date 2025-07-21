package cz.rohlik.assignment.backend.rest;

import cz.rohlik.assignment.backend.model.ProductRequestDto;
import cz.rohlik.assignment.backend.model.ProductResponseDto;
import cz.rohlik.assignment.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping(ProductController.PATH)
@Validated
public class ProductController {

    public static final String PATH = "/api/v1/products";
    public static final String ID_PATH = "/{id}";

    ProductService productService;

    @GetMapping
    public Page<ProductResponseDto> getAllProducts(@ParameterObject Pageable pageable) {
        return productService.getAll(pageable);
    }

    @GetMapping(ID_PATH)
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public ProductResponseDto createProduct(
        @RequestBody @Valid ProductRequestDto productRequestDto
    ) {
        return productService.createProduct(productRequestDto);
    }

    @PutMapping(ID_PATH)
    public ProductResponseDto updateProduct(
        @PathVariable Long id,
        @RequestBody @Valid ProductRequestDto productRequestDto
    ) {
        return productService.updateProduct(id, productRequestDto);
    }

    @DeleteMapping(ID_PATH)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
