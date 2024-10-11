import org.example.entities.Category;
import org.example.entities.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {
    private static final LocalDate now = LocalDate.now();

    @Test
    void testValidProduct() {
        Product product = new Product("1", "Shirt", Category.SHIRT, 8, now, now);
        assertNotNull(product);
    }

    @Test
    void testNotEmptyOrNullName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product("5", "", Category.JEANS, 7, now, now));
        assertThrows(IllegalArgumentException.class, () ->
                new Product("5", null, Category.JEANS, 7, now, now));
    }

    @Test
    void testNotEmptyOrNullId() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product("", "Shirt", Category.SHIRT, 7, now, now));
        assertThrows(IllegalArgumentException.class, () ->
                new Product(null, "Shirt", Category.SHIRT, 7, now, now));
    }

    @Test
    void testNotRatingTooHighOrLow() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product("1", "Shirt", Category.SHIRT, 17, now, now));
        assertThrows(IllegalArgumentException.class, () ->
                new Product(null, "Shirt", Category.SHIRT, -1, now, now));
    }

    @Test
    void testNotNullCategory() {
        assertThrows(NullPointerException.class, () ->
                new Product("5", "Shirt", null, 7, now, now));
    }

    @Test
    void testNotNullCreationDate() {
        assertThrows(NullPointerException.class, () ->
                new Product("5", "Shirt", Category.SHIRT, 7, null, now));
    }

    @Test
    void testNotNullModificationDate() {
        assertThrows(NullPointerException.class, () ->
                new Product("5", "Shirt", Category.SHIRT, 7, now, null));
    }
}
