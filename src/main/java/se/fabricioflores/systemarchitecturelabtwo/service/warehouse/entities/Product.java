package se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonbPropertyOrder({
        "id",
        "name",
        "category",
        "rating"
})
// ** The canonical constructor is meant for testing (don't use it) ** //
public record Product (
        @PositiveOrZero int id,
        @NotNull @NotEmpty(message = "Name field can't be empty") String name,
        @NotNull(message = "Category field can't be empty") Category category,
        @Min(1) @Max(10) int rating,
        LocalDateTime createdAt,
        LocalDateTime editedAt)
{

    // ** Preferred constructor for creating new products ** //
    // Jsonb allows the application to accept this record and use this specific constructor.
    @JsonbCreator
    public Product(
            @JsonbProperty("id")  int id,
            @JsonbProperty("name") String name,
            @JsonbProperty("category") Category category,
            @JsonbProperty("rating") int rating)
    {
        this(id, name, category, rating, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0));
    }

    // Product ids must be unique
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

