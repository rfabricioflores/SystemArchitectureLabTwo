package se.fabricioflores.systemarchitecturelabtwo.resource;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import se.fabricioflores.systemarchitecturelabtwo.service.RequestResponse;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.Warehouse;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;
import se.fabricioflores.systemarchitecturelabtwo.util.ErrorEntity;
import se.fabricioflores.systemarchitecturelabtwo.util.DataEntity;


import static jakarta.ws.rs.core.Response.Status.*;

@Path("/product")
@Produces("application/json")
@Consumes("application/json")
public class ProductResource {

    private Warehouse warehouse;
    private ResponseBuilder response;

    @Context private UriInfo uriInfo;

    @Inject
    public ProductResource(Warehouse warehouse, RequestResponse requestResponse) {
        this.warehouse = warehouse;
        this.response = requestResponse.getRes();
    }

    public ProductResource() {}

    private String getPath() {
        String basePath = uriInfo.getBaseUri().getPath();
        String requestPath = uriInfo.getPath(false);
        return UriBuilder.fromPath(basePath).path(requestPath).build().toString();
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") @NotNull int productId) {
        warehouse.getProductByID(productId).ifPresentOrElse(product -> {
            response.status(OK).entity(new DataEntity(product, "Found product successfully!"));
        }, () -> {
            response.status(NOT_FOUND).entity(new ErrorEntity("Product not found", getPath()));
        });

        return response.build();
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

    @POST
    public Response addProduct(@NotNull Product product) {
        try {
            warehouse.addProduct(product);
            response
                    .status(OK)
                    .entity(new DataEntity(product, "Product added successfully!"));
        } catch (IllegalArgumentException e) {
            response
                    .status(NOT_ACCEPTABLE)
                    .entity(new ErrorEntity(e.getMessage(), getPath()));
        }

        return response.build();
    }
}
