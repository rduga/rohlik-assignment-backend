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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemService orderItemService;

    public Page<OrderResponseDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    public OrderResponseDto getById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setStatus(OrderStatus.RESERVED);

        order = orderRepository.save(order);

        Order orderForItems = order;
        order.setItems(orderRequestDto.getItems().stream()
            .map((OrderItemDto orderItemDto) -> orderItemService.createOrderItem(orderItemDto, orderForItems))
            .collect(Collectors.toCollection(ArrayList::new)));

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderResponseDto payForOrder(Long id, PaymentRequestDto paymentRequestDto) {
        return updateOrder(id, order -> {
            if (order.isPaid() || order.isCancelled()) {
                throw new OrderCannotBeCancelledException("Cannot cancel a paid or cancelled order");
            }
            order.setStatus(OrderStatus.PAID);
        });
    }

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

    private OrderResponseDto updateOrder(Long id, Consumer<Order> orderConsumer) {
        Order order = orderRepository.getById(id);

        orderConsumer.accept(order);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }
}
