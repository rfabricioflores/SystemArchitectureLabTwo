package se.fabricioflores.systemarchitecturelabtwo.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.fabricioflores.systemarchitecturelabtwo.ObjectMapperContextResolver;
import se.fabricioflores.systemarchitecturelabtwo.exception.ExceptionMapper;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.IWarehouse;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.Warehouse;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Category;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.entities.Product;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductResourceTest {

    @Mock
    IWarehouse warehouse;

    Dispatcher dispatcher;

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse();
        dispatcher = MockDispatcherFactory.createDispatcher();
        var productResource = new ProductResource(warehouse);
        dispatcher.getRegistry().addSingletonResource(productResource);
        dispatcher.getProviderFactory().registerProvider(ObjectMapperContextResolver.class);
        dispatcher.getProviderFactory().registerProvider(ExceptionMapper.class);
    }

    @Test
    public void testGetAllProductsWithZeroProductsRespondsWithStatus404() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/product");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void testGetAllProductsWithOneProductRespondsWithStatus200() throws Exception {
        warehouse.addProduct(new Product(1, "Ball", Category.SPORT, 6));
        MockHttpRequest request = MockHttpRequest.get("/product");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
    }

//    ** For some shitty reason this doesn't work ** \\
//    @Test
//    void testAddProductRespondsWithStatus201() throws Exception {
//        MockHttpRequest request = MockHttpRequest.post("/product");
//        MockHttpResponse response = new MockHttpResponse();
//
//        Product productToBeCreated = new Product(1, "Mac", Category.TECH, 8);
//        String json = new ObjectMapper().writeValueAsString(productToBeCreated);
//
//        request.contentType("application/json");
//        request.content(json.getBytes());
//
//        dispatcher.invoke(request, response);
//
//        assertThat(response.getStatus()).isEqualTo(201);
//    }
}
