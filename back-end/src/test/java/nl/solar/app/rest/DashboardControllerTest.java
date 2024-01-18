package nl.solar.app.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for testing the HTTP endpoints of the DashboardController
 *
 * @author Hanan Ouardi
 */
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc; //To make a requests to the controllers, without servers and to make http requests


    /**
     * Tests the endpoint "/dashboard-items/inventory" in DashboardController
     * Verifies that the response status is OK, not empty and a non-null value
     *
     * @throws Exception if there is an error during the test.
     */
    @Test
    public void testGetDashboardItems() throws Exception {
        //to perform assertions on result
        ResultActions result = mockMvc.perform(get("/dashboard-items/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].warehouseId", notNullValue()))
                .andExpect(jsonPath("$[0].warehouseName", notNullValue()))
                .andExpect(jsonPath("$[0].productName", notNullValue()))
                .andExpect(jsonPath("$[0].quantity", notNullValue()))
                .andExpect(jsonPath("$[0].inventoryQuantity", notNullValue()))
                .andExpect(jsonPath("$[0].deliverDate", notNullValue()));
    }

    /**
     * Tests the endpoint "/dashboard-items/inventory" in DashboardController
     * Verifies that the response status is OK, not empty and a non-null value
     *
     * @throws Exception if there is an error during the test.
     */
    @Test
    public void testGetProjectDashboardItems() throws Exception {
        ResultActions result = mockMvc.perform(get("/dashboard-items/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].dueDate", notNullValue()))
                .andExpect(jsonPath("$[0].projectId", notNullValue()))
                .andExpect(jsonPath("$[0].projectName", notNullValue()))
                .andExpect(jsonPath("$[0].warehouseId", notNullValue()))
                .andExpect(jsonPath("$[0].warehouseName", notNullValue()))
                .andExpect(jsonPath("$[0].productId", notNullValue()))
                .andExpect(jsonPath("$[0].productName", notNullValue()))
                .andExpect(jsonPath("$[0].amountOfProduct", notNullValue()));
    }

    /**
     * Tests the endpoint "/dashboard-items/inventory-quantity" in DashboardController
     * Verifies that the response status is OK, not empty and a non-null value
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    public void testGetInventoryQuantity() throws Exception {
         mockMvc.perform(get("/dashboard-items/inventory-quantity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].warehouseId").isNotEmpty())
                .andExpect(jsonPath("$[0].warehouseName").isNotEmpty())
                .andExpect(jsonPath("$[0].productName").isNotEmpty())
                .andExpect(jsonPath("$[0].inventoryQuantity").isNotEmpty())
                .andExpect(jsonPath("$[0].productId").isNotEmpty());
    }

}
