package nl.solar.app.rest;

import nl.solar.app.WebConfig;
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

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the WarehouseController
 * @author Wilco van de Pol
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WarehouseControllerTest {

    @Autowired
    private WebConfig webConfig;

    @Autowired
    TestRestTemplate restTemplate;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    @BeforeEach
    public void setup() {
        webConfig.SECURED_PATHS = Set.of("/empty");
        if (servletContextPath == null) {
            servletContextPath = "/";
        }
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void allWarehousesCanBeRetrieved(){
        ResponseEntity<Warehouse[]> response =
                this.restTemplate.getForEntity("/warehouses", Warehouse[].class);

        // check status code and the response body
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 'OK'");
        Warehouse[] warehouses = response.getBody();
        assertThat(warehouses.length, is(greaterThan(0)));
    }

    @Test
    public void specificWarehouseCanBeRetrieved() {
        ResponseEntity<Warehouse> response =
                this.restTemplate.getForEntity("/warehouses/1000", Warehouse.class);

        // check status code and the response body
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 'OK'");
        Warehouse warehouse = response.getBody();

        if (warehouse != null) {
            assertEquals(warehouse.getName(), "Solar Sedum");
            assertEquals(warehouse.getLocation(), "H.J.E. Wenckebachweg 47D, 1096AK Amsterdam");
        }
    }

    @Test
    public void aNewWarehouseCanBePosted() {
        Warehouse warehouse = new Warehouse(
                1234,
                "Test Name",
                "Test Location");

        ResponseEntity<Warehouse> response1 =
                this.restTemplate.postForEntity("/warehouses", warehouse,Warehouse.class);

        // check status code and response body
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        Warehouse newWarehouse = response1.getBody();
        if (newWarehouse != null){
            assertThat(newWarehouse.getId(), is(greaterThan(0L)));
            assertEquals(warehouse.getName(), newWarehouse.getName());
        }

        // check location
        String location = response1.getHeaders().getLocation().getPath();
        assertThat(location, is(equalTo(servletContextPath + "warehouses/" + newWarehouse.getId())));

        // retrieve the book that was just posted and verify
        ResponseEntity<Warehouse> response2 =
                this.restTemplate.getForEntity("/warehouses/" + newWarehouse.getId(), Warehouse.class);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        Warehouse savedWarehouse = response2.getBody();
        assertEquals(savedWarehouse.getName(), warehouse.getName());
    }
}
