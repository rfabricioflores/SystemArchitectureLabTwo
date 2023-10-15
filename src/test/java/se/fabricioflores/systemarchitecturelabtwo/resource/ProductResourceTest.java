package se.fabricioflores.systemarchitecturelabtwo.resource;

import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.fabricioflores.systemarchitecturelabtwo.service.warehouse.WarehouseService;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductResourceTest {

    @Mock
    WarehouseService warehouse;

    Dispatcher dispatcher;

    @BeforeEach
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        var productResource = new ProductResource(warehouse);
        dispatcher.getRegistry().addSingletonResource(productResource);
    }

    @Test
    public void testSomething() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/product");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertThat(200).isEqualTo(response.getStatus());
    }
}
