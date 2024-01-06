package nl.solar.app.rest;

import nl.solar.app.DTO.InventoryDTO;
import nl.solar.app.DTO.InventoryProductDTO;
import nl.solar.app.WebConfig;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Product;
import nl.solar.app.models.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class inventoryControllerTest {

    @Autowired
    WebConfig webConfig;


    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    @BeforeEach
    public void setup() {
        webConfig.SECURED_PATHS = Set.of("/empty"); //changes the webconfig, so that the test don't need authentication
        if (servletContextPath == null) {
            servletContextPath = "/";
        }
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void allInventoryCanBeRetrieved() {
        ResponseEntity<InventoryDTO[]> response = this.restTemplate
                .getForEntity("/inventory", InventoryDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        InventoryDTO[] inventory = response.getBody();
        assertThat(inventory.length, is(greaterThan(0)));
    }

    @Test
    public void allInventoryForWarehouseCanBeRetrieved() {
        ResponseEntity<InventoryProductDTO[]> response = this.restTemplate
                .getForEntity("/warehouses/1000/inventory", InventoryProductDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ;
        InventoryProductDTO[] inventory = response.getBody();
        assertThat(inventory.length, is(greaterThan(0)));
    }

    @Test
    public void InventoryCanBeCreated() {
        //first create a new product and get a existing warehouse. These are needed to create a new inventory
        ResponseEntity<Warehouse> warehouseResponse = this.restTemplate
                .getForEntity("/warehouses/1000", Warehouse.class);

        Product newProduct = new Product();
        newProduct.setProductName("Product");
        newProduct.setDescription("Product description");

        ResponseEntity<Product> productResponse = this.restTemplate
                .postForEntity("/products", newProduct, Product.class);

        Warehouse warehouse = warehouseResponse.getBody();
        Product product = productResponse.getBody();
        Inventory inventory = Inventory.createDummyInventory(warehouse, product);

        ResponseEntity<Inventory> response = this.restTemplate
                .postForEntity("/inventory", inventory, Inventory.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ;
        Inventory inventoryResponse = response.getBody();

        //check if the response is the same as the inventory that was posted
        assertEquals(inventory.getWarehouse().getId(), inventoryResponse.getWarehouse().getId(), "Warehouse id should be the same");
        assertEquals(inventory.getProduct().getId(), inventoryResponse.getProduct().getId(), "Product id should be the same");
        assertEquals(inventory.getQuantity(), inventoryResponse.getQuantity(), "Quantity should be the same");
        assertEquals(inventory.getMinimum(), inventoryResponse.getMinimum(), "Minimum should be the same");

        String location = response.getHeaders().getLocation().getPath();
        String expectedLocation = "/warehouses/" + inventoryResponse.getWarehouse().getId() + "/products/" + inventoryResponse.getProduct().getId();
        assertEquals(expectedLocation, location);
    }

    @Test
    public void createProductReturnErrorIfProductOrWarehouseIsNotSet() {
        //first create a new product and get a existing warehouse. These are needed to create a new inventory
        ResponseEntity<Warehouse> warehouseResponse = this.restTemplate
                .getForEntity("/warehouses/1000", Warehouse.class);

        ResponseEntity<Product> productResponse = this.restTemplate
                .getForEntity("/products/1", Product.class);

        Warehouse warehouse = warehouseResponse.getBody();
        Product product = productResponse.getBody();
        Product nullProduct = null;
        Warehouse nullWarehouse = null;

        Inventory inventory = Inventory.createDummyInventory(warehouse, nullProduct);
        ResponseEntity<BadRequestException> response = this.restTemplate
                .postForEntity("/inventory", inventory, BadRequestException.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        BadRequestException exception = response.getBody();
        assertEquals("Product is not set", exception.getMessage());

        inventory = Inventory.createDummyInventory(nullWarehouse, product);
        response = this.restTemplate
                .postForEntity("/inventory", inventory, BadRequestException.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        exception = response.getBody();
        assertEquals("Warehouse is not set", exception.getMessage());
    }

    @Test
    public void GetProductsWithoutInventoryReturnsEmptyList() {
        //The dataloader creates inventory for all products, so list should be empty
        ResponseEntity<InventoryProductDTO[]> response = this.restTemplate
                .getForEntity("/warehouses/1000/products/without-inventory", InventoryProductDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ;
        InventoryProductDTO[] inventory = response.getBody();
        assertThat(inventory.length, is(0));
    }

    @Test
    public void patchRequestShouldUpdateInventory() {
        Map<String, Integer> patch = Map.of("quantity", 10, "minimum", 5);
        ResponseEntity<Inventory> response = this.restTemplate.exchange(
                "/warehouses/1000/products/1",
                org.springframework.http.HttpMethod.PATCH,
                new org.springframework.http.HttpEntity<>(patch),
                Inventory.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Inventory inventory = response.getBody();
        assertEquals(patch.get("quantity").longValue(), inventory.getQuantity());
        assertEquals(patch.get("minimum"), inventory.getMinimum());
    }

    @Test
    public void patchRequestShouldReturnBadRequestWhenQuantityOrMinimumIsNotSet() {
        Map<String, Integer> patch = Map.of("minimum", 5);
        ResponseEntity<BadRequestException> response = this.restTemplate.exchange(
                "/warehouses/1000/products/1",
                org.springframework.http.HttpMethod.PATCH,
                new org.springframework.http.HttpEntity<>(patch),
                BadRequestException.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        BadRequestException exception = response.getBody();
        assertEquals("Request body doesn't contain quantity", exception.getMessage());

        patch = Map.of("quantity", 10);
        response = this.restTemplate.exchange(
                "/warehouses/1000/products/1",
                org.springframework.http.HttpMethod.PATCH,
                new org.springframework.http.HttpEntity<>(patch),
                BadRequestException.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        exception = response.getBody();
        assertEquals("Request body doesn't contain quantity", exception.getMessage());
    }

}
