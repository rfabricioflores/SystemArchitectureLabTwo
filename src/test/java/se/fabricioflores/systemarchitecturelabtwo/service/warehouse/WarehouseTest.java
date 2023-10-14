package se.fabricioflores.systemarchitecturelabtwo.service.warehouse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;

import java.time.LocalDateTime;


public class WarehouseTest {
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    // Add Product Tests
    @Test
    void testAddProduct() {
        var product = new Product(1, "Product 1", Category.TECH, 5);
        warehouse.addProduct(product);

        var allProducts = warehouse.getAllProducts();

        assertThat(allProducts).hasSize(1);
        assertThat(allProducts).containsExactly(product);
    }

    @Test
    void testAddProductWithEmptyName() {
        var product = new Product(1, "", Category.SPORT, 5);

        assertThatThrownBy(() -> warehouse.addProduct(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name field can't be empty");

        var allProducts = warehouse.getAllProducts();

        assertThat(allProducts).hasSize(0);
    }

    @Test
    void testAddTwoProductsWithSameId() {
        var product1 = new Product(1, "Ring", Category.JEWELRY, 2);
        var product2 = new Product(1, "Ball", Category.SPORT, 5);

        assertThatThrownBy(() -> {
            warehouse.addProduct(product1);
            warehouse.addProduct(product2);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Another product with this id already exists");

        var allProducts = warehouse.getAllProducts();

        assertThat(allProducts).hasSize(1);
        assertThat(allProducts).containsExactly(product1);
    }

    // Edit Product Tests
    @Test
    void testEditProduct() {
        var product = new Product(1, "Product One", Category.SPORT, 2);
        warehouse.addProduct(product);

        var optionalModifiedProduct = warehouse.editProduct(1, "Edited Product", Category.TECH, 3);

        assertThat(optionalModifiedProduct).isPresent();

        var modifiedProduct = optionalModifiedProduct.get();
        assertThat(modifiedProduct.name()).isEqualTo("Edited Product");
        assertThat(modifiedProduct.category()).isEqualTo(Category.TECH);
        assertThat(modifiedProduct.rating()).isEqualTo(3);
    }

    @Test
    void testEditProductThatDoesNotExist() {
        warehouse.editProduct(1, "Not existing product", Category.SPORT, 1);
        var products = warehouse.getAllProducts();

        assertThat(products).hasSize(0);
    }

    @Test
    void testEditProductWithEmptyName() {
        Product product = new Product(1, "Product", Category.SPORT, 5);
        warehouse.addProduct(product);

        assertThatThrownBy(() -> {
            warehouse.editProduct(1, "", Category.TECH, 5);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name field can't be empty");

        var optionalProduct = warehouse.getProductByID(1);
        assertThat(optionalProduct).isPresent();

        Product updatedProduct = optionalProduct.get();
        assertThat(updatedProduct.name()).isEqualTo("Product");
    }

    // Get all products tests
    @Test
    void testGetAllProducts() {
        warehouse.addProduct(new Product(1, "Ring", Category.JEWELRY, 5));
        warehouse.addProduct(new Product(2, "Ball", Category.SPORT, 5));

        var products = warehouse.getAllProducts();
        assertThat(products).hasSize(2);
    }

    @Test
    void testGetAllProductsFromEmptyWarehouse() {
        var products = warehouse.getAllProducts();
        assertThat(products).hasSize(0);
    }

    // Get product by id tests
    @Test
    void testGetProductById() {
        var product = new Product(1, "Product 1", Category.TECH, 8);
        warehouse.addProduct(product);

        var optionalProduct = warehouse.getProductByID(1);
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get()).isEqualTo(product);
    }

    // Get products by category and sort in alphabetic order
    @Test
    void testGetProductsByCategoryAndSortedByAlphabeticOrder() {
        warehouse.addProduct(new Product(1, "Product C", Category.TECH, 8));
        warehouse.addProduct(new Product(2, "Product A", Category.JEWELRY, 7));
        warehouse.addProduct(new Product(3, "Product B", Category.TECH, 9));

        var categoryTechProducts = warehouse.getProductsByCategorySortedByName(Category.TECH);
        assertThat(categoryTechProducts).hasSize(2);
        assertThat(categoryTechProducts).extracting(Product::name).containsExactly("Product B", "Product C");
    }

    // Get products created after a date test
    @Test
    void testGetProductsCreatedAfterDate() {
        var currentDate = LocalDateTime.now();
        var pastDate = currentDate.minusDays(1);
        var futureDate = currentDate.plusDays(1);

        warehouse.addProduct(new Product(1, "Product 1", Category.TECH, 8, pastDate, pastDate));
        warehouse.addProduct(new Product(2, "Product 2", Category.JEWELRY, 5, futureDate, futureDate));
        warehouse.addProduct(new Product(3, "Product 3", Category.SPORT, 9, currentDate, currentDate));

        var productsRecentlyCreated = warehouse.getProductsCreatedAfterDate(currentDate);
        assertThat(productsRecentlyCreated).hasSize(1);
        assertThat(productsRecentlyCreated.get(0).name()).isEqualTo("Product 2");
    }

    // Get products that have been modified after creation test
    @Test
    void testGetProductsModifiedAfterCreation() {
        var currentDate = LocalDateTime.now();
        var pastDate = currentDate.minusDays(1);
        var futureDate = currentDate.plusDays(1);

        warehouse.addProduct(new Product(1, "Product 1", Category.TECH, 8, pastDate, pastDate));
        warehouse.addProduct(new Product(2, "Product 2", Category.JEWELRY, 7, futureDate, futureDate));
        warehouse.addProduct(new Product(3, "Product 3", Category.SPORT, 9, currentDate, futureDate));

        var productsModifiedAfterCreation = warehouse.getProductsModifiedAfterCreation();
        assertThat(productsModifiedAfterCreation).hasSize(1);
        assertThat(productsModifiedAfterCreation.get(0).name()).isEqualTo("Product 3");
    }

    // VG Tester
    @Test
    void testGetCategoriesWithOneOrMoreProducts() {
        warehouse.addProduct(new Product(1, "Product A", Category.SPORT, 2));
        warehouse.addProduct(new Product(2, "Product B", Category.TECH, 7));
        warehouse.addProduct(new Product(3, "Product C", Category.SPORT, 9));
        warehouse.addProduct(new Product(4, "Product D", Category.SPORT, 9));

        var categories = warehouse.getCategoriesWithOneOrMoreProducts();
        assertThat(categories).hasSize(2);
        assertThat(categories).contains(Category.SPORT, Category.TECH);
    }

    @Test
    void testGetProductCountOfCategory() {
        warehouse.addProduct(new Product(1, "Product 1", Category.JEWELRY, 2));
        warehouse.addProduct(new Product(2, "Product 2", Category.TECH, 7));
        warehouse.addProduct(new Product(3, "Product 3", Category.JEWELRY, 9));

        int jewelryProductCount = warehouse.getProductCountOfCategory(Category.JEWELRY);
        assertThat(jewelryProductCount).isEqualTo(2);
    }

    @Test
    void testGetProductMapOfNameFirstCharAndQuantity() {
        warehouse.addProduct(new Product(1, "Arc", Category.TECH, 2));
        warehouse.addProduct(new Product(2, "Boat", Category.TECH, 1));
        warehouse.addProduct(new Product(3, "Banana", Category.TECH, 4));
        warehouse.addProduct(new Product(4, "Basic", Category.TECH, 6));
        warehouse.addProduct(new Product(5, "Car", Category.TECH, 9));

        var charCountMap = warehouse.getProductMapOfNameFirstCharAndQuantity();
        assertThat(charCountMap).hasSize(3);
        assertThat(charCountMap.get('A')).isEqualTo(1);
        assertThat(charCountMap.get('B')).isEqualTo(3);
        assertThat(charCountMap.get('C')).isEqualTo(1);
    }

    @Test
    void testGetMaxRatedProductsCreatedThisMonthSortedByNewest() {
        LocalDateTime thisMonth = LocalDateTime.now();
        LocalDateTime lastMonth = thisMonth.minusMonths(1);

        warehouse.addProduct(new Product(1, "Product A", Category.SPORT, 10, thisMonth, thisMonth));
        warehouse.addProduct(new Product(2, "Product B", Category.SPORT, 9, thisMonth, thisMonth));
        warehouse.addProduct(new Product(3, "Product C", Category.SPORT, 10, lastMonth, lastMonth));
        warehouse.addProduct(new Product(4, "Product D", Category.SPORT, 10, lastMonth, lastMonth));

        var maxRatedProductsThisMonth = warehouse.getMaxRatedProductsCreatedThisMonthSortedByNewest();

        assertThat(maxRatedProductsThisMonth).hasSize(1);
        assertThat(maxRatedProductsThisMonth.get(0).rating()).isEqualTo(10);
    }
}
