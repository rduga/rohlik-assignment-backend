package cz.rohlik.assignment.backend.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Abstract base class for all JPA entities in the application.
 * <p>
 * Provides common fields for ID, creation, and update timestamps, and handles their lifecycle events.
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    /**
     * Primary key identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Timestamp when the entity was created.
     */
    Instant createdAt;

    /**
     * Timestamp when the entity was last updated.
     */
    Instant updatedAt;

    /**
     * Sets the creation timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    /**
     * Updates the update timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
