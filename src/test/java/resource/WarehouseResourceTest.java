package resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.*;
import jakarta.ws.rs.core.MediaType;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.rest.WarehouseResource;
import org.example.service.WarehouseService;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import config.CustomJacksonProvider;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseResourceTest {
    private final LocalDate now = LocalDate.now();
    private final Product product = new Product("1", "Shirt", Category.SHIRT, 5, now, now);

    Dispatcher dispatcher;

    @Mock
    WarehouseService warehouseService;

    @InjectMocks
    WarehouseResource warehouseResource;


    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    private JSONObject convertProductToJson(Product product) throws JsonProcessingException, JSONException {
        ObjectMapper mapper = getObjectMapper();
        String jsonString = mapper.writeValueAsString(product);
        return new JSONObject(jsonString);
    }

    @BeforeEach
    public void setUp() {
        warehouseService = new WarehouseService();
        warehouseResource = new WarehouseResource(warehouseService);
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getRegistry().addSingletonResource(warehouseResource);
        CustomJacksonProvider customJacksonProvider = new CustomJacksonProvider();
        dispatcher.getProviderFactory().registerProviderInstance(customJacksonProvider);
    }

    @Test
    void getAllProductsTest() throws URISyntaxException, UnsupportedEncodingException, JSONException, JsonProcessingException {
        MockHttpRequest request = MockHttpRequest.get("/products");
        MockHttpResponse response = new MockHttpResponse();
        warehouseService.addProduct(product);
        dispatcher.invoke(request, response);

        JSONArray expectedJson = new JSONArray();
        expectedJson.put(convertProductToJson(product));
        JSONArray responseJson = new JSONArray(response.getContentAsString());

        assertEquals(expectedJson.toString(), responseJson.toString());
        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldFindProductAndReturnStatus200() throws URISyntaxException, UnsupportedEncodingException, JSONException, JsonProcessingException {
        MockHttpRequest request = MockHttpRequest.get("/products/1");
        MockHttpResponse response = new MockHttpResponse();
        warehouseService.addProduct(product);
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
        JSONObject expectedJson = convertProductToJson(product);
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        assertEquals(expectedJson.toString(), responseJson.toString());
    }

    @Test
    void shouldNotFindProductAndReturnStatus404() throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.get("/products/145e6");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void shouldReturnProductsByCategoryShirt() throws URISyntaxException, UnsupportedEncodingException, JSONException, JsonProcessingException {
        MockHttpRequest request = MockHttpRequest.get("/products/category/shirt");
        MockHttpResponse response = new MockHttpResponse();
        Product otherCategoryProduct = new Product("2", "Shirt", Category.JEANS, 5, now, now);
        warehouseService.addProduct(product);
        warehouseService.addProduct(otherCategoryProduct);
        dispatcher.invoke(request, response);
        JSONArray expected = new JSONArray();
        expected.put(convertProductToJson(product));
        JSONArray responseJson = new JSONArray(response.getContentAsString());
        assertEquals(expected.toString(), responseJson.toString());
        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldAddProductAndReturnStatus201() throws URISyntaxException, JsonProcessingException {
        LocalDate now = LocalDate.now();
        MockHttpRequest request = MockHttpRequest.post("/products");
        MockHttpResponse response = new MockHttpResponse();

        ObjectMapper objectMapper = getObjectMapper();

        String json = objectMapper.writeValueAsString(new Product("651", "Shirt", Category.SHIRT, 5, now, now));
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);

        dispatcher.invoke(request, response);
        assertEquals(201, response.getStatus());
    }

    @SuppressWarnings("All")
    @Test
    void shouldThrowConstraintViolationExceptionWhenInvalidProduct() {
        Product invalidProduct = new Product("1", "", Category.SHIRT, 0, now, now);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(invalidProduct);

        assertEquals(2, violations.size());
        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertTrue(messages.contains("Rating must be between 1 and 10"));
        assertTrue(messages.contains("Name must not be blank"));
        factory.close();
    }

    @Test
    void shouldReturnBadRequestForDuplicateProductId() throws URISyntaxException, JsonProcessingException {
        warehouseService.addProduct(product);

        MockHttpRequest request = MockHttpRequest.post("/products");
        MockHttpResponse response = new MockHttpResponse();

        ObjectMapper objectMapper = getObjectMapper();

        String json = objectMapper.writeValueAsString(product);
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);

        dispatcher.invoke(request, response);

        assertEquals(400, response.getStatus());
    }
}
