package cz.rohlik.assignment.backend.rest;

import cz.rohlik.assignment.backend.model.OrderRequestDto;
import cz.rohlik.assignment.backend.model.OrderResponseDto;
import cz.rohlik.assignment.backend.model.PaymentRequestDto;
import cz.rohlik.assignment.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Operation(
        summary = "Get all orders",
        description = """
            Retrieves a paginated (or additionally sorted) list of all orders.
            
            The response includes order details such as ID, status, items and total price.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Order list retrieved successfully")
        }
    )
    @GetMapping
    public Page<OrderResponseDto> getAllOrders(@ParameterObject Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @Operation(
        summary = "Get order by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @GetMapping(ID_PATH)
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @Operation(
        summary = "Create a new order",
        description = """
            Creates a new order with the provided details.
            The order is initially in the 'RESERVED' status and can be paid for or cancelled later.
            
            The order will expire after a predefined time if not paid.
            """,
        responses = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Order creation failed due to validation errors"),
            @ApiResponse(responseCode = "404", description = "Any order product not found")
        }
    )
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
        @Valid @RequestBody OrderRequestDto orderRequestDto,
        UriComponentsBuilder uriBuilder
    ) {
        OrderResponseDto responseDto = orderService.createOrder(orderRequestDto);

        UriComponents uriComponents = uriBuilder.path(PATH + ID_PATH)
            .buildAndExpand(responseDto.getId());

        return ResponseEntity.created(uriComponents.toUri())
            .body(responseDto);
    }

    @Operation(
        summary = "Pay for an order",
        description = """
            Pays for an existing order by ID with the provided payment details.
            The order must be in 'RESERVED' status and not already paid or cancelled.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Order paid successfully"),
            @ApiResponse(responseCode = "400", description = "Order creation failed due to validation errors"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @PostMapping(PAY_FOR_ORDER_PATH)
    public OrderResponseDto payForOrder(@PathVariable Long id, @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        return orderService.payForOrder(id, paymentRequestDto);
    }

    @Operation(
        summary = "Cancel an order",
        description = """
            Cancels an existing order by ID.
            The order must be in 'RESERVED' status and not already paid or cancelled.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Order paid successfully"),
            @ApiResponse(responseCode = "400", description = "Order creation failed due to validation errors"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @PostMapping(CANCEL_ORDER_PATH)
    public OrderResponseDto cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }
}
