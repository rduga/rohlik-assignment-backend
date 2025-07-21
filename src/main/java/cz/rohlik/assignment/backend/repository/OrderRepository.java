package cz.rohlik.assignment.backend.repository;

import cz.rohlik.assignment.backend.entity.Order;
import cz.rohlik.assignment.backend.model.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.Instant;

public interface OrderRepository extends CrudRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    Page<Order> findAllByStatusAndCreatedAtBefore(OrderStatus orderStatus, Instant instant, Pageable pageable);
}
