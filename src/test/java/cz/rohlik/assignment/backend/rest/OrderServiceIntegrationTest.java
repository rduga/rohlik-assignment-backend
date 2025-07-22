package cz.rohlik.assignment.backend.rest;

import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.model.OrderItemDto;
import cz.rohlik.assignment.backend.model.OrderRequestDto;
import cz.rohlik.assignment.backend.model.OrderResponseDto;
import cz.rohlik.assignment.backend.model.OrderStatus;
import cz.rohlik.assignment.backend.model.ProductRequestDto;
import cz.rohlik.assignment.backend.model.ProductResponseDto;
import cz.rohlik.assignment.backend.repository.OrderRepository;
import cz.rohlik.assignment.backend.service.OrderService;
import cz.rohlik.assignment.backend.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Test
    void checkForExpiredOrders_shouldCancelExpiredOrdersAndReleaseReservedStockItems() {

        ProductResponseDto productResponseDto1 = productService.createProduct(ProductRequestDto.builder()
            .name("Test Product " + System.currentTimeMillis())
            .pricePerUnit(new BigDecimal("10.00"))
            .stockQuantity(new BigDecimal("100"))
            .build());

        ProductResponseDto productResponseDto2 = productService.createProduct(ProductRequestDto.builder()
            .name("Test Product " + System.currentTimeMillis())
            .pricePerUnit(new BigDecimal("10.00"))
            .stockQuantity(new BigDecimal("100"))
            .build());

        OrderResponseDto orderResponseDto = orderService.createOrder(OrderRequestDto.builder()
            .items(List.of(
                OrderItemDto.builder()
                    .productId(productResponseDto1.getId())
                    .quantity(new BigDecimal("100"))
                    .build(),
                OrderItemDto.builder()
                    .productId(productResponseDto2.getId())
                    .quantity(new BigDecimal("100"))
                    .build()
            ))
            .build());

        Assertions.assertThat(productService.getById(productResponseDto1.getId()).getStockQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
        Assertions.assertThat(productService.getById(productResponseDto2.getId()).getStockQuantity()).isEqualByComparingTo(BigDecimal.ZERO);

        Order expiredOrder = orderRepository.getById(orderResponseDto.getId());
        expiredOrder.setCreatedAt(Instant.now().minus(2, ChronoUnit.HOURS));
        expiredOrder = orderRepository.save(expiredOrder);


        orderService.checkForExpiredOrders();


        Order updatedOrder = orderRepository.findById(expiredOrder.getId()).orElseThrow();

        Assertions.assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        Assertions.assertThat(productService.getById(productResponseDto1.getId()).getStockQuantity()).isEqualByComparingTo(new BigDecimal("100"));
        Assertions.assertThat(productService.getById(productResponseDto2.getId()).getStockQuantity()).isEqualByComparingTo(new BigDecimal("100"));
    }
}
