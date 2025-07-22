package cz.rohlik.assignment.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Entity representing a product available for ordering.
 * <p>
 * Contains product name, price per unit, stock quantity, and version for optimistic locking.
 */
@Entity
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    /**
     * Name of the product.
     */
    String name;

    /**
     * Price per unit of the product.
     */
    BigDecimal pricePerUnit;

    /**
     * Quantity of the product available in stock.
     */
    BigDecimal stockQuantity;

    /**
     * Version field for optimistic locking.
     */
    @Version
    Integer version;
}
