package org.example.entities;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record Product(
        @NotBlank(message = "ID must not be blank")
        String id,
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotNull(message = "Category must not be blank")
        Category category,
        @Min(value = 1, message = "Rating must be between 1 and 10")
        @Max(value = 10, message = "Rating must be between 1 and 10")
        int rating,
        @PastOrPresent
        LocalDate creationDate,
        @PastOrPresent
        LocalDate lastModifiedDate
) {
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
