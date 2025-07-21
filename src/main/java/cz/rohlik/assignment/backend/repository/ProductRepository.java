package cz.rohlik.assignment.backend.repository;

import cz.rohlik.assignment.backend.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }
}
