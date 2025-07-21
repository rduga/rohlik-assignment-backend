package cz.rohlik.assignment.backend.repository;

import cz.rohlik.assignment.backend.entity.Order;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends CrudRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }
}
