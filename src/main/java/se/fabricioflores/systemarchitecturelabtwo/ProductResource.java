package se.fabricioflores.systemarchitecturelabtwo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/product")
public class ProductResource {

    @GET
    @Produces("text/plain")
    public String response() {
        return "product";
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getProduct(@PathParam("id") String productId) {
        var response = Response.notModified();

        if(productId == null || productId.isEmpty()) {
            response.status(404).entity(new Object() {
                public final String error = "Not found";
            });
        } else {
            response.status(200).entity(
                    new Object() {
                        public final String id = productId;
                        public final String name = "product " + productId;
                    }
            );
        }

        return response.build();
    }
}
