package cz.rohlik.assignment.backend.service;

import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.exception.OrderCannotBeCancelledException;
import cz.rohlik.assignment.backend.model.OrderDto;
import cz.rohlik.assignment.backend.model.OrderItemDto;
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

    public Page<OrderDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    public OrderDto getById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setStatus(Order.OrderStatus.RESERVED);

        order = orderRepository.save(order);

        Order orderForItems = order;
        order.setItems(orderDto.getItems().stream()
            .map((OrderItemDto orderItemDto) -> orderItemService.createOrderItem(orderItemDto, orderForItems))
            .collect(Collectors.toCollection(ArrayList::new)));

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDto payForOrder(Long id, PaymentRequestDto paymentRequestDto) {
        return updateOrder(id, order -> {
            if (order.isPaid() || order.isCancelled()) {
                throw new OrderCannotBeCancelledException("Cannot cancel a paid or cancelled order");
            }
            order.setStatus(Order.OrderStatus.PAID);
        });
    }

    @Transactional
    public OrderDto cancelOrder(Long id) {
        return updateOrder(id, order -> {
            if (order.isPaid()) {
                throw new OrderCannotBeCancelledException("Cannot cancel a paid order");
            }
            order.setStatus(Order.OrderStatus.CANCELLED);
        });
    }

    private OrderDto updateOrder(Long id, Consumer<Order> orderConsumer) {
        Order order = orderRepository.getById(id);

        orderConsumer.accept(order);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }
}
