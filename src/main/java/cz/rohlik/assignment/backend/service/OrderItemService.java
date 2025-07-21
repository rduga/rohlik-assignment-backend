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

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderItemService {

    ProductRepository productRepository;
    OrderItemRepository orderItemRepository;

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

    @Transactional
    public void cancelOrderItems(List<OrderItem> items) {
        items.forEach(orderItem -> {
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity().add(orderItem.getQuantity()));
            productRepository.save(product);
        });
    }
}
