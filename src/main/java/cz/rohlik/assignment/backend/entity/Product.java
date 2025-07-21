package cz.rohlik.assignment.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    String name;

    BigDecimal pricePerUnit;

    BigDecimal stockQuantity;

    @Version
    Integer version;
}
