package cz.rohlik.assignment.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rohlik.assignment.backend.model.ProductRequestDto;
import cz.rohlik.assignment.backend.service.ProductService;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    public static final ProductRequestDto PRODUCT_REQUEST_DTO = ProductRequestDto.builder()
        .name("New Product " + System.currentTimeMillis())
        .pricePerUnit(new BigDecimal("10.00"))
        .stockQuantity(new BigDecimal("100"))
        .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Test
    public void createProduct_shouldReturnCreated_whenProductIsValid() throws Exception {
        createProduct(mockMvc, objectMapper);
    }

    @SneakyThrows
    public static Long createProduct(MockMvc mockMvc, ObjectMapper objectMapper) {
        String locationHeader =
            mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PRODUCT_REQUEST_DTO)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn()
            .getResponse()
            .getHeader("Location");

        Assertions.assertThat(locationHeader).isNotEmpty();

        return getIdFromLocationHeader(locationHeader);
    }

    public static Long getIdFromLocationHeader(String locationHeader) {
        return Long.valueOf(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));
    }

    @Test
    public void getAllProducts_shouldReturnPageOfProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void getProductById_shouldReturnProduct_whenProductExists() throws Exception {
        Long productId = createProduct(mockMvc, objectMapper);

        mockMvc.perform(get("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    public void updateProduct_shouldReturnUpdatedProduct_whenProductIsValid() throws Exception {
        Long productId = createProduct(mockMvc, objectMapper);

        ProductRequestDto requestDto = ProductRequestDto.builder()
            .name("Updated Product")
            .pricePerUnit(new BigDecimal("15.00"))
            .stockQuantity(new BigDecimal("50"))
            .build();

        mockMvc.perform(put("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    public void deleteProduct_shouldReturnOk_whenProductExists() throws Exception {
        Long productId = createProduct(mockMvc, objectMapper);

        mockMvc.perform(delete("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}