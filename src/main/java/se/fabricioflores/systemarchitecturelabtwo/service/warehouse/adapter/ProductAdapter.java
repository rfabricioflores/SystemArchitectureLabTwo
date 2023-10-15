package se.fabricioflores.systemarchitecturelabtwo.service.warehouse.adapter;

import jakarta.json.JsonObject;
import jakarta.json.bind.adapter.JsonbAdapter;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;

public class ProductAdapter implements JsonbAdapter<Product, JsonObject> {
    @Override
    public JsonObject adaptToJson(Product product) {
        return null;
    }

    // ** Converts category to uppercase before it creates a new instance of product ** \\
    @Override
    public Product adaptFromJson(JsonObject adapted) {
        var id = adapted.getInt("id");
        var name = adapted.getString("name");
        var rating = adapted.getInt("rating");
        var category = Category.valueOf(adapted.getString("category").toUpperCase());

        return new Product(id, name, category, rating);
    }
}
