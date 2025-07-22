package cz.rohlik.assignment.backend.service;


import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.entity.OrderItem;
import cz.rohlik.assignment.backend.entity.OrderItemId;
import cz.rohlik.assignment.backend.entity.Product;
import cz.rohlik.assignment.backend.exception.InsufficientStockForProductException;
import cz.rohlik.assignment.backend.model.OrderItemDto;
import cz.rohlik.assignment.backend.repository.OrderItemRepository;
import cz.rohlik.assignment.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing order items and related business logic.
 * <p>
 * Handles creation of order items, stock management, and cancellation of order items.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderItemService {

    /**
     * Repository for accessing product data.
     */
    ProductRepository productRepository;
    /**
     * Repository for accessing order item data.
     */
    OrderItemRepository orderItemRepository;

    /**
     * Creates a new order item for the given order and updates product stock.
     *
     * @param orderItemDto the order item DTO containing product and quantity
     * @param order the order to which the item belongs
     * @return the created OrderItem entity
     * @throws InsufficientStockForProductException if there is not enough stock for the product
     */
    @Transactional
    public OrderItem createOrderItem(OrderItemDto orderItemDto, Order order) {
        Product product = productRepository.getById(orderItemDto.getProductId());

        if (product.getStockQuantity().compareTo(orderItemDto.getQuantity()) < 0) {
            throw new InsufficientStockForProductException(
                "Insufficient stock for product: %s, available quantity: %s, requested quantity: %s"
                    .formatted(product.getName(), product.getStockQuantity(), orderItemDto.getQuantity()));
        }

        product.setStockQuantity(product.getStockQuantity().subtract(orderItemDto.getQuantity()));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(new OrderItemId(order.getId(), product.getId()));
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDto.getQuantity());

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        productRepository.save(product);

        return savedOrderItem;
    }

    /**
     * Cancels the given order items and restores their quantities to product stock.
     *
     * @param items the list of order items to cancel
     */
    @Transactional
    public void cancelOrderItems(List<OrderItem> items) {
        items.forEach(orderItem -> {
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity().add(orderItem.getQuantity()));
            productRepository.save(product);
        });
    }

    /**
     * Checks if any order item exists for the given product ID.
     *
     * @param productId the product ID to check
     * @return true if an order item exists for the product, false otherwise
     */
    public boolean orderItemExistsByProductId(Long productId) {
        return orderItemRepository.existsByProductId(productId);
    }
}
