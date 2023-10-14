package se.fabricioflores.systemarchitecturelabtwo.service.warehouse;

import jakarta.ejb.Lock;
import jakarta.ejb.Singleton;
import jakarta.ejb.LockType;
import jakarta.enterprise.context.ApplicationScoped;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Singleton
@ApplicationScoped
public class Warehouse {
    private final List<Product> productList = new ArrayList<>();

    @Lock(LockType.WRITE)
    public void addProduct(Product product) {
        if (product.name().isEmpty()) throw new IllegalArgumentException("Name field can't be empty");
        if (product.rating() > 10 || product.rating() < 1) throw new IllegalArgumentException("Rating must be between 1 and 10");
        if (productList.contains(product)) throw new IllegalArgumentException("Another product with this id already exists");
        productList.add(product);
    }

    @Lock(LockType.WRITE)
    public Optional<Product> editProduct(int id, String name, Category category, int rating) {
        if (name.isEmpty()) throw new IllegalArgumentException("Name field can't be empty");
        if (rating > 10 || rating < 1) throw new IllegalArgumentException("Rating must be between 1 and 10");
        if (category == null) throw new IllegalArgumentException("Category field can't be null");

        var optionalProductToEdit = productList.stream().filter(p -> p.id() == id).findFirst();

        if(optionalProductToEdit.isPresent()) {
            Product productToEdit = optionalProductToEdit.get();
            Product modifiedProduct = new Product(
                    productToEdit.id(),
                    name,
                    category,
                    rating,
                    productToEdit.createdAt(),
                    LocalDateTime.now()
            );

            productList.remove(productToEdit);
            productList.add(modifiedProduct);

            return Optional.of(modifiedProduct);
        }

        return Optional.empty();
    }

    @Lock(LockType.READ)
    public List<Product> getAllProducts() {
        return productList;
    }

    @Lock(LockType.READ)
    public Optional<Product> getProductByID(int productId) {
        return productList.stream().filter(p -> p.id() == productId).findFirst();
    }

    @Lock(LockType.READ)
    public List<Product> getProductsByCategorySortedByName(Category category) {
        return productList.stream()
                .filter(p -> p.category() == category)
                .sorted(Comparator.comparing(Product::name))
                .toList();
    }

    @Lock(LockType.READ)
    public List<Product> getProductsCreatedAfterDate(LocalDateTime date) {
        return productList.stream()
                .filter(p -> p.createdAt().isAfter(date))
                .toList();
    }

    @Lock(LockType.READ)
    public List<Product> getProductsModifiedAfterCreation() {
         return productList.stream()
                    .filter(p -> !p.editedAt().equals(p.createdAt()))
                    .toList();
    }

    @Lock(LockType.READ)
    public List<Category> getCategoriesWithOneOrMoreProducts() {
        return productList.stream()
                .map(Product::category)
                .distinct()
                .toList();
    }

    @Lock(LockType.READ)
    public int getProductCountOfCategory(Category category) {
        return (int) productList.stream()
                .filter(p -> p.category() == category)
                .count();
    }

    @Lock(LockType.READ)
    public Map<Character, Long> getProductMapOfNameFirstCharAndQuantity() {
        return productList.stream()
                .map(p -> p.name().toUpperCase().charAt(0))
                .collect(
                        Collectors.groupingBy(
                                letter -> letter,
                                Collectors.counting()
                        )
                );
    }

    @Lock(LockType.READ)
    public List<Product> getMaxRatedProductsCreatedThisMonthSortedByNewest() {
        return productList.stream()
                .filter(p -> p.createdAt().getMonth() == YearMonth.now().getMonth())
                .filter(p -> p.rating() == 10)
                .sorted(Comparator.comparing(Product::createdAt).reversed())
                .toList();
    }
}