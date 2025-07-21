package cz.rohlik.assignment.backend.rest;

import cz.rohlik.assignment.backend.model.OrderRequestDto;
import cz.rohlik.assignment.backend.model.OrderResponseDto;
import cz.rohlik.assignment.backend.model.PaymentRequestDto;
import cz.rohlik.assignment.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping(OrderController.PATH)
@Validated
@Slf4j
public class OrderController {

    public static final String PATH = "/api/v1/orders";
    public static final String ID_PATH = "/{id}";
    public static final String CANCEL_ORDER_PATH = ID_PATH + "/cancel";
    public static final String PAY_FOR_ORDER_PATH = ID_PATH + "/pay";

    OrderService orderService;

    @GetMapping
    public Page<OrderResponseDto> getAllOrders(@ParameterObject Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @GetMapping(ID_PATH)
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @PostMapping(PAY_FOR_ORDER_PATH)
    public OrderResponseDto payForOrder(@PathVariable Long id, @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        return orderService.payForOrder(id, paymentRequestDto);
    }

    @PostMapping(CANCEL_ORDER_PATH)
    public OrderResponseDto cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }
}
