package cz.rohlik.assignment.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rohlik.assignment.backend.model.OrderItemDto;
import cz.rohlik.assignment.backend.model.OrderRequestDto;
import cz.rohlik.assignment.backend.model.PaymentRequestDto;
import cz.rohlik.assignment.backend.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static cz.rohlik.assignment.backend.rest.ProductControllerIntegrationTest.getIdFromLocationHeader;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;  // Assume this is properly mocked or tested

    @Test
    public void createOrder_shouldReturnCreated_whenOrderIsValid() throws Exception {
        createOrder();
    }

    private Long createOrder() throws Exception {
        Long productId1 = ProductControllerIntegrationTest.createProduct(mockMvc, objectMapper);
        Long productId2 = ProductControllerIntegrationTest.createProduct(mockMvc, objectMapper);

        OrderRequestDto requestDto = OrderRequestDto.builder()
            .items(List.of(
                OrderItemDto.builder()
                    .productId(productId1)
                    .quantity(BigDecimal.valueOf(100))
                    .build(),
                OrderItemDto.builder()
                    .productId(productId2)
                    .quantity(BigDecimal.valueOf(100))
                    .build()
            ))
            .build();

        String locationHeader = mockMvc.perform(post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        Assertions.assertThat(locationHeader).isNotEmpty();

        return getIdFromLocationHeader(locationHeader);
    }

    @Test
    public void getAllOrders_shouldReturnPageOfOrders() throws Exception {
        createOrder();

        mockMvc.perform(get("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void getOrderById_shouldReturnOrder_whenOrderExists() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(get("/api/v1/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderId));
    }

    @Test
    public void payForOrder_shouldReturnUpdatedOrder_whenPaymentIsValid() throws Exception {
        Long orderId = createOrder();

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
            .paymentMethod("CREDIT_CARD")
            .paymentDetails("Card details here")
            .amount(new BigDecimal("2000.00"))
            .build();

        mockMvc.perform(post("/api/v1/orders/{id}/pay", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    public void cancelOrder_shouldReturnUpdatedOrder_whenOrderIsCancellable() throws Exception {
        Long orderId = createOrder();

        mockMvc.perform(post("/api/v1/orders/{id}/cancel", orderId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}