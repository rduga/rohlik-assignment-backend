package cz.rohlik.assignment.backend.entity;

import cz.rohlik.assignment.backend.model.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
// 'order' is a reserved keyword in SQL, so we use "orders" as the table name
@Table(name = "orders")
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @OneToMany(mappedBy = "order")
    List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @Version
    Integer version;

    @Transient
    public boolean isPaid() {
        return OrderStatus.PAID.equals(status);
    }

    @Transient
    public boolean isCancelled() {
        return OrderStatus.CANCELLED.equals(status);
    }

}
