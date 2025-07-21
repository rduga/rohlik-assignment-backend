package cz.rohlik.assignment.backend.rest;

import cz.rohlik.assignment.backend.model.ProductRequestDto;
import cz.rohlik.assignment.backend.model.ProductResponseDto;
import cz.rohlik.assignment.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping(ProductController.PATH)
@Validated
public class ProductController {

    public static final String PATH = "/api/v1/products";
    public static final String ID_PATH = "/{id}";

    ProductService productService;

    @Operation(
        summary = "Get all products",
        description = """
            Retrieves a paginated (or additionally sorted) list of all products.
            
            The response includes product details such as ID, name, description, price, and stock quantity.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Product list retrieved successfully")
        }
    )
    @GetMapping
    public Page<ProductResponseDto> getAllProducts(@ParameterObject Pageable pageable) {
        return productService.getAll(pageable);
    }

    @Operation(
        summary = "Get product by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        }
    )
    @GetMapping(ID_PATH)
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(
        summary = "Create a new product",
        description = """
            Creates a new product with the provided details.
            The request body must contain the product name, description, price, and stock quantity.
            
            The response will include the created product's ID and other details.
            """,
        responses = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Product creation failed due to validation errors"),
        }
    )
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
        @RequestBody @Valid ProductRequestDto productRequestDto,
        UriComponentsBuilder uriBuilder
    ) {
        ProductResponseDto responseDto = productService.createProduct(productRequestDto);

        UriComponents uriComponents = uriBuilder.path(PATH + ID_PATH)
            .buildAndExpand(responseDto.getId());

        return ResponseEntity.created(uriComponents.toUri())
            .body(responseDto);
    }

    @Operation(
        summary = "Update an existing product",
        description = """
            Updates an existing product with the provided details.
            The request body must contain the product name, description, price, and stock quantity.
            
            The response will include the updated product's ID and other details.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Product update failed due to validation errors"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        }
    )
    @PutMapping(ID_PATH)
    public ProductResponseDto updateProduct(
        @PathVariable Long id,
        @RequestBody @Valid ProductRequestDto productRequestDto
    ) {
        return productService.updateProduct(id, productRequestDto);
    }

    @Operation(
        summary = "Delete an existing product",
        description = """
            Deletes an existing product with the provided id.
            
            The product must not have any associated order items.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
        }
    )
    @DeleteMapping(ID_PATH)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
