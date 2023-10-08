package se.fabricioflores.systemarchitecturelabtwo.service.warehouse;

import jakarta.enterprise.context.ApplicationScoped;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@ApplicationScoped
public class Warehouse {
    private final Lock lock = new ReentrantLock();
    private final List<Product> productList = new ArrayList<>();

    public void addProduct(Product product) {
        lock.lock();
        try {
            if (product.name().isEmpty()) throw new IllegalArgumentException("Name field can't be empty");
            if (product.rating() > 10 || product.rating() < 1) throw new IllegalArgumentException("Rating must be between 1 and 10");
            if (productList.contains(product)) throw new IllegalArgumentException("Another product with this id already exists");
            productList.add(product);
        } finally {
            lock.unlock();
        }
    }

    public void editProduct(int id, String name, Category category, int rating) {
        lock.lock();
        try {
            if (name.isEmpty()) throw new IllegalArgumentException("Name field can't be empty");
            if (rating > 10 || rating < 1) throw new IllegalArgumentException("Rating must be between 1 and 10");
            if (category == null) throw new IllegalArgumentException("Category field can't be null");

            var optionalProduct = productList.stream().filter(p -> p.id() == id).findFirst();

            optionalProduct.ifPresent(product -> {
                Product editedProduct = new Product(
                        product.id(),
                        name,
                        category,
                        rating,
                        product.createdAt(),
                        LocalDateTime.now()
                );
                productList.remove(product);
                productList.add(editedProduct);
            });
        } finally {
            lock.unlock();
        }
    }

    public List<Product> getAllProducts() {
        lock.lock();

        try {
            return productList;
        } finally {
            lock.unlock();
        }
    }

    public Optional<Product> getProductByID(int productId) {
        lock.lock();

        try {
            return productList.stream().filter(p -> p.id() == productId).findFirst();
        } finally {
            lock.unlock();
        }
    }

    public List<Product> getProductsByCategorySortedByName(Category category) {
        lock.lock();

        try {
            return productList.stream()
                    .filter(p -> p.category() == category)
                    .sorted(Comparator.comparing(Product::name))
                    .toList();
        } finally {
            lock.unlock();
        }

    }

    public List<Product> getProductsCreatedAfterDate(LocalDateTime date) {
        lock.lock();

        try {
            return productList.stream()
                    .filter(p -> p.createdAt().isAfter(date))
                    .toList();
        } finally {
            lock.unlock();
        }

    }

    public List<Product> getProductsModifiedAfterCreation() {
        lock.lock();

        try {
            return productList.stream()
                    .filter(p -> !p.editedAt().equals(p.createdAt()))
                    .toList();
        } finally {
            lock.unlock();
        }

    }

    public List<Category> getCategoriesWithOneOrMoreProducts() {
        lock.lock();

        try {
            return productList.stream()
                    .map(Product::category)
                    .distinct()
                    .toList();
        } finally {
            lock.unlock();
        }

    }

    public int getProductCountOfCategory(Category category) {
        lock.lock();

        try {
            return (int) productList.stream()
                    .filter(p -> p.category() == category)
                    .count();
        } finally {
            lock.unlock();
        }

    }

    public Map<Character, Long> getProductMapOfNameFirstCharAndQuantity() {
        lock.lock();

        try {
            return productList.stream()
                    .map(p -> p.name().toUpperCase().charAt(0))
                    .collect(
                            Collectors.groupingBy(
                                    letter -> letter,
                                    Collectors.counting()
                            )
                    );
        } finally {
            lock.unlock();
        }

    }

    public List<Product> getMaxRatedProductsCreatedThisMonthSortedByNewest() {
        lock.lock();

        try {
            return productList.stream()
                    .filter(p -> p.createdAt().getMonth() == YearMonth.now().getMonth())
                    .filter(p -> p.rating() == 10)
                    .sorted(Comparator.comparing(Product::createdAt).reversed())
                    .toList();
        } finally {
            lock.unlock();
        }

    }
}