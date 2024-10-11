package org.example.entities;

import java.time.LocalDate;
import java.util.Objects;

public record Product(
        String id,
        String name,
        Category category,
        int rating,
        LocalDate creationDate,
        LocalDate lastModifiedDate
) {
    public Product {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product id cannot be null or blank");
        }
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Product rating must be between 0 and 10");
        }
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(creationDate, "Creation date cannot be null");
        Objects.requireNonNull(lastModifiedDate, "Last modification date cannot be null");
    }

    @Override
    public String toString() {
        return "Product ID: " + id +
                ", Name: " + name +
                ", Category: " + category +
                ", Rating: " + rating +
                ", Creation Date: " + creationDate +
                ", Last Modified Date: " + lastModifiedDate;
    }
}
