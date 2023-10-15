package se.fabricioflores.systemarchitecturelabtwo.service.warehouse;

import jakarta.ejb.Singleton;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public interface WarehouseService {
    void addProduct(Product product);
    Optional<Product> editProduct(int id, String name, Category category, int rating);
    List<Product> getAllProducts();
    Optional<Product> getProductByID(int productId);
    List<Product> getProductsByCategorySortedByName(Category category);
    List<Product> getProductsCreatedAfterDate(LocalDateTime date);
    List<Product> getProductsModifiedAfterCreation();
    List<Category> getCategoriesWithOneOrMoreProducts();
    int getProductCountOfCategory(Category category);
    Map<Character, Long> getProductMapOfNameFirstCharAndQuantity();
    List<Product> getMaxRatedProductsCreatedThisMonthSortedByNewest();
}
