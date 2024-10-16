package service;

import org.example.entities.Category;
import org.example.entities.Product;
import org.example.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseServiceTest {
    private WarehouseService warehouseService;
    private final LocalDate now = LocalDate.now();
    private final List<Product> products = Arrays.asList(
            new Product("1", "Shirt", Category.SHIRT, 8, now.minusDays(2), now.minusDays(1)),
            new Product("2", "Hoodie", Category.HOODIE, 6, now.minusDays(5), now.minusDays(5)),
            new Product("3", "jeans", Category.JEANS, 10, now.minusDays(2), now.minusDays(2)),
            new Product("4", "Other Jeans", Category.JEANS, 10, now.minusDays(32), now.minusDays(31)),
            new Product("5", "ZZZ", Category.JEANS, 8, now, now),
            new Product("6", "aaa", Category.JEANS, 8, now, now)
    );
    private final int TOTAL_PRODUCTS = products.size();
    private final List<Category> categories = new ArrayList<>();

    @BeforeEach
    void setUp() {
        warehouseService = new WarehouseService();
        products.forEach(product -> {
            warehouseService.addProduct(product);
            if (!categories.contains(product.category())) {
                categories.add(product.category());
            }
        });
    }

    @Test
    void testAddProduct() {
        assertEquals(TOTAL_PRODUCTS, warehouseService.getAllProducts().size());
        Product product = new Product("123", "Shirt", Category.SHIRT, 8, LocalDate.now(), LocalDate.now());
        warehouseService.addProduct(product);
        assertEquals(TOTAL_PRODUCTS + 1, warehouseService.getAllProducts().size());
    }

    @Test
    void throwsWhenDuplicateIdProducts() {
        Product product = new Product("1", "Shirt", Category.SHIRT, 8, LocalDate.now(), LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> warehouseService.addProduct(product));
        assertEquals(TOTAL_PRODUCTS, warehouseService.getAllProducts().size());
    }

    @Test
    void testModifyProduct() {
        warehouseService.modifyProduct("1", "New Shirt", Category.SHIRT, 9);
        Optional<Product> modifiedProduct = warehouseService.getProductById("1");
        modifiedProduct.ifPresent(product -> {
            assertEquals("New Shirt", product.name());
            assertEquals(9, product.rating());
            assertTrue(product.creationDate().isBefore(product.lastModifiedDate()));
        });
    }

    @Test
    void testGetProductById() {
        Product product = new Product("1", "Shirt", Category.SHIRT, 8, now.minusDays(2), now.minusDays(1));
        Optional<Product> optionalProduct = warehouseService.getProductById("1");
        assertTrue(optionalProduct.isPresent());
        assertEquals(product, optionalProduct.get());
    }

    @Test
    void testEmptyOptionalWhenIDNotFound() {
        assertTrue(warehouseService.getProductById("000").isEmpty());
    }

    @Test
    void testGetProductsByCategorySortedByName() {
        assertEquals(
                List.of("aaa", "jeans", "Other Jeans", "ZZZ"),
                warehouseService.getProductsByCategory(Category.JEANS)
                        .stream()
                        .map(Product::name)
                        .collect(Collectors.toList())
        );
    }

    @Test
    void testGetProductsCreatedAfter() {
        assertEquals(4, warehouseService.getProductsCreatedAfter(LocalDate.now().minusDays(3)).size());
    }

    @Test
    void testGetModifiedProducts() {
        List<Product> products = warehouseService.getModifiedProducts();
        assertEquals(2, products.size());
        assertTrue(products.stream().allMatch(product -> product.lastModifiedDate().isAfter(product.creationDate())));
    }

    @Test
    void testGetNonEmptyCategories() {
        assertEquals(categories, warehouseService.getNonEmptyCategories());
    }

    @Test
    void testGetNumberProductsOfCategory() {
        assertEquals(1, warehouseService.getNumberProductsByCategory(Category.SHIRT));
    }

    @Test
    void testGetProductStartingLetterMap() {
        Map<Character, Long> map = warehouseService.getProductStartingLetterMap();
        assertEquals(1, map.get('S'));
        assertEquals(1, map.get('H'));
    }

    @Test
    void testGetMaxRatedProductsLastMonth() {
        List<Product> products = warehouseService.getMaxRatedProductsLastMonth();
        assertEquals(1, products.size());
        assertTrue(products.stream().allMatch(product ->
                product.rating() == 10 && product.creationDate().isAfter(LocalDate.now().minusMonths(1))));
    }
}