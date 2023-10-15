package se.fabricioflores.systemarchitecturelabtwo.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import se.fabricioflores.systemarchitecturelabtwo.interceptor.Log;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.IWarehouse;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;
import se.fabricioflores.systemarchitecturelabtwo.util.ErrorEntity;
import se.fabricioflores.systemarchitecturelabtwo.util.DataEntity;

import static jakarta.ws.rs.core.Response.Status.*;

@Log
@Path("/product")
@Produces("application/json")
@Consumes("application/json")
public class ProductResource {

    private IWarehouse warehouse;

    @RequestScoped
    private final ResponseBuilder response = Response.notModified();

    @Context
    private UriInfo uriInfo;

    @Inject
    public ProductResource(IWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public ProductResource() {}

    private String getPath() {
        String basePath = uriInfo.getBaseUri().getPath();
        String requestPath = uriInfo.getPath(false);
        return UriBuilder.fromPath(basePath).path(requestPath).build().toString();
    }

    @GET
    @Path("/")
    public Response getAllProducts() {
        var products = warehouse.getAllProducts();

        return response
                .status(OK)
                .entity(new DataEntity(products, "Retrieved products successfully"))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int productId) {
        warehouse.getProductByID(productId).ifPresentOrElse(product -> {
            response.status(OK).entity(new DataEntity(product, "Found product successfully!"));
        }, () -> {
            response.status(NOT_FOUND).entity(new ErrorEntity("Product not found", getPath()));
        });

        return response.build();
    }

    @GET
    @Path("/category/{name}")
    public Response getProductByCategory(@PathParam("name") String category) {
        try {
            Category categoryEnum = Category.valueOf(category.toUpperCase());
            var products = warehouse.getProductsByCategorySortedByName(categoryEnum);
            if(products.isEmpty()) {
                response
                        .status(NOT_FOUND)
                        .entity(new DataEntity(products, "Could not find products with the category " + category));
            } else {
                response
                        .status(OK)
                        .entity(new DataEntity(products, "Retrieved products successfully!"));
            }
        } catch (IllegalArgumentException e) {
            response
                    .status(NOT_ACCEPTABLE)
                    .entity(new ErrorEntity("Not valid category", getPath()));
        }

        return response.build();
    }

    @POST
    @Path("/")
    public Response addProduct(@Valid Product product) {
        try {
            warehouse.addProduct(product);
            response
                    .status(OK)
                    .entity(new DataEntity(product, "Product created successfully!"));
        } catch (IllegalArgumentException e) {
            response
                    .status(NOT_ACCEPTABLE)
                    .entity(new ErrorEntity(e.getMessage(), getPath()));
        }

        return response.build();
    }

    @PUT
    @Path("/")
    public Response editProduct(@Valid Product product) {
        try {
            var modifiedProduct = warehouse.editProduct(product.id(), product.name(), product.category(), product.rating());
            if(modifiedProduct.isPresent()) {
                response
                        .status(ACCEPTED)
                        .entity(new DataEntity(modifiedProduct, "Product edited successfully"));
            } else {
                response
                        .status(NOT_ACCEPTABLE)
                        .entity(new ErrorEntity("Invalid product id", getPath()));
            }
        } catch (IllegalArgumentException e) {
            response
                    .status(NOT_ACCEPTABLE)
                    .entity(new ErrorEntity(e.getMessage(), getPath()));
        }

        return response.build();
    }
}
