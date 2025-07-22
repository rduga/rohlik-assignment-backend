package cz.rohlik.assignment.backend.service;

import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.exception.OrderCannotBeCancelledException;
import cz.rohlik.assignment.backend.model.OrderItemDto;
import cz.rohlik.assignment.backend.model.OrderRequestDto;
import cz.rohlik.assignment.backend.model.OrderResponseDto;
import cz.rohlik.assignment.backend.model.OrderStatus;
import cz.rohlik.assignment.backend.model.PaymentRequestDto;
import cz.rohlik.assignment.backend.model.mapper.OrderMapper;
import cz.rohlik.assignment.backend.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Service class for managing orders and related business logic.
 * <p>
 * Handles order creation, payment, cancellation, and expiration.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class OrderService {

    /**
     * Repository for accessing order data.
     */
    OrderRepository orderRepository;
    /**
     * Mapper for converting between Order entities and DTOs.
     */
    OrderMapper orderMapper;
    /**
     * Service for managing order items.
     */
    OrderItemService orderItemService;

    /**
     * Order expiration time in seconds (default: 1800 seconds = 30 minutes).
     */
    @Value("${app.order.expiration.time-in-seconds:1800}") // Default to 30 minutes if not set
    long orderExpirationTimeSeconds;

    /**
     * Retrieves all orders with pagination support.
     *
     * @param pageable pagination and sorting information
     * @return paginated list of order response DTOs
     */
    public Page<OrderResponseDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the order ID
     * @return the order response DTO
     * @throws EntityNotFoundException if the order is not found
     */
    public OrderResponseDto getById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    /**
     * Creates a new order with the provided details.
     *
     * @param orderRequestDto the order request DTO
     * @return the created order response DTO
     */
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setStatus(OrderStatus.RESERVED);

        order = orderRepository.save(order);

        Order orderForItems = order;
        order.setItems(orderRequestDto.getItems().stream()
            .map((OrderItemDto orderItemDto) -> orderItemService.createOrderItem(orderItemDto, orderForItems))
            .collect(Collectors.toCollection(ArrayList::new)));

        order.setTotalPrice(order.getItems().stream()
            .map(item -> item.getProduct().getPricePerUnit().multiply(item.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Pays for an existing order by ID with the provided payment details.
     *
     * @param id the order ID
     * @param paymentRequestDto the payment request DTO
     * @return the updated order response DTO
     */
    @Transactional
    public OrderResponseDto payForOrder(Long id, PaymentRequestDto paymentRequestDto) {
        return updateOrder(id, order -> {
            if (order.isPaid() || order.isCancelled()) {
                throw new OrderCannotBeCancelledException("Cannot cancel a paid or cancelled order");
            }

            if (paymentRequestDto.getAmount().compareTo(order.getTotalPrice()) != 0) {
                throw new OrderCannotBeCancelledException("Payment amount: %s does not match order total price: %s"
                    .formatted(paymentRequestDto.getAmount(), order.getTotalPrice()));
            }

            order.setStatus(OrderStatus.PAID);
        });
    }

    /**
     * Cancels an existing order by ID.
     *
     * @param id the order ID
     * @return the updated order response DTO
     */
    @Transactional
    public OrderResponseDto cancelOrder(Long id) {
        return updateOrder(id, order -> {
            if (order.isPaid()) {
                throw new OrderCannotBeCancelledException("Cannot cancel a paid order");
            }

            if (order.isCancelled()) {
                throw new OrderCannotBeCancelledException("Order is already cancelled");
            }

            orderItemService.cancelOrderItems(order.getItems());

            order.setStatus(OrderStatus.CANCELLED);
        });
    }

    /**
     * Updates an order using the provided consumer function.
     *
     * @param id the order ID
     * @param orderConsumer the consumer function to apply to the order
     * @return the updated order response DTO
     */
    private OrderResponseDto updateOrder(Long id, Consumer<Order> orderConsumer) {
        Order order = orderRepository.getById(id);

        orderConsumer.accept(order);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    /**
     * Checks for and cancels expired orders that have not been paid.
     */
    @Transactional
    public void checkForExpiredOrders() {
        Page<Order> expiredOrders = orderRepository.findAllByStatusAndCreatedAtBefore(
                OrderStatus.RESERVED, getCreatedAtBefore(), Pageable.unpaged());

        if (!expiredOrders.hasContent()) {
            return;
        }

        expiredOrders.getContent().forEach(order -> {
            log.info("Cancelling expired order with id: {}", order.getId());
            order.setStatus(OrderStatus.CANCELLED);
            orderItemService.cancelOrderItems(order.getItems());
        });
        orderRepository.saveAll(expiredOrders.getContent());
    }

    /**
     * Calculates the cutoff timestamp for order expiration.
     *
     * @return the cutoff Instant
     */
    private Instant getCreatedAtBefore() {
        return Instant.now().minus(orderExpirationTimeSeconds, ChronoUnit.SECONDS);
    }
}
