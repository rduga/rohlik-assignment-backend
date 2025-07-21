package cz.rohlik.assignment.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderItem {

    @EmbeddedId
    OrderItemId id;

    @ManyToOne
    @MapsId("orderId")
    @ToString.Exclude
    Order order;

    @ManyToOne
    @MapsId("productId")
    Product product;

    BigDecimal quantity;
}
