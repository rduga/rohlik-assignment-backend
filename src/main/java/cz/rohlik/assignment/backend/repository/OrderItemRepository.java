package cz.rohlik.assignment.backend.repository;

import cz.rohlik.assignment.backend.entity.OrderItem;
import cz.rohlik.assignment.backend.entity.OrderItemId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, OrderItemId>, PagingAndSortingRepository<OrderItem, OrderItemId> {

    default OrderItem getById(OrderItemId id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + id));
    }
}
