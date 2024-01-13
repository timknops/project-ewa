package nl.solar.app.rest;


import nl.solar.app.DTO.ItemDTO;
import nl.solar.app.DTO.OrderRequestDTO;
import nl.solar.app.DTO.OrderUpdateTestDTO;
import nl.solar.app.WebConfig;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Item;
import nl.solar.app.models.Order;
import nl.solar.app.models.Product;
import nl.solar.app.models.Warehouse;
import nl.solar.app.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @Autowired
    WebConfig webConfig;

    @MockBean
    InventoryService inventoryService;

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
    }

    @Test
    public void allOrdersCanBeRetrieved() {
        ResponseEntity<Order[]> response = this.restTemplate
                .getForEntity("/orders", Order[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody().length, greaterThan(0));
    }

    @Test
    public void orderCanBeRetrievedById() {
        ResponseEntity<Order> response = this.restTemplate
                .getForEntity("/orders/1", Order.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), 1);
    }

    @Test
    public void itemsCanBeRetrievedForOrder() {
        ResponseEntity<Item[]> response = this.restTemplate
                .getForEntity("/orders/1/items", Item[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Item[] items = response.getBody();
        assertThat(items.length, greaterThan(0));

        for (Item item : items) {
            assertEquals(item.getOrder().getId(), 1);
        }
    }

    @Test
    public void orderCanBeCreated() {
        Warehouse warehouse = this.restTemplate.getForObject("/warehouses/1000", Warehouse.class);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setDeliverDate(LocalDate.now().plusDays(12));
        orderRequestDTO.setTag("tag");
        orderRequestDTO.setWarehouse(warehouse);

        List<ItemDTO> items = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemDTO item = new ItemDTO();
            item.setProduct(this.restTemplate.getForObject("/products/" + (i + 1), Product.class));
            item.setQuantity((long) (Math.random() * 20));
            items.add(item);
        }
        orderRequestDTO.setItems(items);

        ResponseEntity<Order> response = this.restTemplate
                .postForEntity("/orders", orderRequestDTO, Order.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();
        String expectedLocation = "/orders/" + response.getBody().getId();
        assertEquals(expectedLocation, location);

        Order savedOrder = response.getBody();
        assertEquals(savedOrder.getTag(), orderRequestDTO.getTag(), "tag should be the same");
        assertEquals(savedOrder.getWarehouse(), orderRequestDTO.getWarehouse(), "warehouse should be the same");
        assertEquals(savedOrder.getDeliverDate(), orderRequestDTO.getDeliverDate(), "deliver date should be the same");
        assertEquals(savedOrder.getStatus(), OrderStatus.PENDING, "Controller should set the status to pending");
        assertNull(savedOrder.getOrderDate(), "Order date should not be returned in the response");

        Item[] savedItems = this.restTemplate.getForObject("/orders/" + savedOrder.getId() + "/items", Item[].class);
        System.out.println(savedItems.length);
        assertEquals(savedItems.length, items.size(), "The number of items should be the same");
    }

    @Test
    public void postMappingSentCorrectErrors() {
        Warehouse warehouse = this.restTemplate.getForObject("/warehouses/1000", Warehouse.class);
        Warehouse nullWarehouse = null;

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setDeliverDate(LocalDate.now().minusDays(12));
        orderRequestDTO.setTag("tag");
        orderRequestDTO.setWarehouse(nullWarehouse);

        List<ItemDTO> items = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemDTO item = new ItemDTO();
            item.setProduct(this.restTemplate.getForObject("/products/" + (i + 1), Product.class));
            item.setQuantity((long) (Math.random() * 20));
        }
        orderRequestDTO.setItems(items);

        ResponseEntity<BadRequestException> response = this.restTemplate
                .postForEntity("/orders", orderRequestDTO, BadRequestException.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getMessage(), "An order should be placed for a warehouse!");

        orderRequestDTO.setWarehouse(warehouse);


        response = this.restTemplate
                .postForEntity("/orders", orderRequestDTO, BadRequestException.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getMessage(), "An order can not be delivered in the past!");
    }

    @Test
    public void orderCanBeUpdated() {
        Order order = this.restTemplate.getForObject("/orders/3", Order.class);

        //copy the order, so that the original order is not chang
        Order updatedOrder = new Order();
        updatedOrder.setId(order.getId());
        updatedOrder.setDeliverDate(order.getDeliverDate().plusDays(12));
        updatedOrder.setStatus(order.getStatus());
        updatedOrder.setTag("Updated tag");
        updatedOrder.setWarehouse(order.getWarehouse());

        //since items are not set the item list for this order will be empty. Therefore, update an order which is not used for other tests
        ResponseEntity<Order> response = this.restTemplate.exchange(
                "/orders/3",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedOrder),
                Order.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), updatedOrder.getId(), "id should stay the same");
        assertEquals(response.getBody().getDeliverDate(), updatedOrder.getDeliverDate(), "deliver date should be updated");
        assertEquals(response.getBody().getTag(), updatedOrder.getTag(), "tag should be updated");
        assertEquals(response.getBody().getWarehouse(), updatedOrder.getWarehouse(), "warehouse stay the same");
        assertEquals(response.getBody().getStatus(), updatedOrder.getStatus(), "status should stay the same");
    }

    @Test
    public void itemListUpdatesWithAnOrder() {
        Order order = this.restTemplate.getForObject("/orders/1", Order.class);

        Set<Item> itemSet = new HashSet<>();
        itemSet.add(new Item(this.restTemplate.getForObject("/products/1", Product.class), order, 10L));
        itemSet.add(new Item(this.restTemplate.getForObject("/products/2", Product.class), order, 20L));

        //mock the order with a DTO, so that the restTemplate can serialize and deserialize the item list
        OrderUpdateTestDTO orderUpdateTestDTO = new OrderUpdateTestDTO(
                order.getId(),
                order.getWarehouse(),
                order.getTag(),
                order.getDeliverDate(),
                order.getStatus(),
                itemSet
        );

        this.restTemplate.put("/orders/1", orderUpdateTestDTO, Order.class);

        Item[] updatedItems = this.restTemplate.getForObject("/orders/1/items", Item[].class);
        assertEquals(updatedItems.length, itemSet.size(), "The number of items should be the same");
        assertThat(Arrays.asList(updatedItems), containsInAnyOrder(itemSet.toArray())); //check if all items are also saved in the database
    }

    @Test
    public void updateInventoryIsCalledCorrectly() {
        //set up new order to ensure that the order is pending
        Warehouse warehouse = this.restTemplate.getForObject("/warehouses/1000", Warehouse.class);
        List<ItemDTO> items = new ArrayList<>();
        ItemDTO item = new ItemDTO();
        item.setProduct(this.restTemplate.getForObject("/products/1", Product.class));
        item.setQuantity(10L);
        ItemDTO item2 = new ItemDTO();
        item2.setProduct(this.restTemplate.getForObject("/products/2", Product.class));
        item2.setQuantity(20L);
        items.add(item);
        items.add(item2);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setDeliverDate(LocalDate.now().plusDays(12));
        orderRequestDTO.setTag("test tag");
        orderRequestDTO.setWarehouse(warehouse);
        orderRequestDTO.setItems(items);

        Order newOrder = this.restTemplate.postForObject("/orders", orderRequestDTO, Order.class);
        Item[] savedItems = this.restTemplate.getForObject("/orders/" + newOrder.getId() + "/items", Item[].class);

        OrderUpdateTestDTO notDeliveredOrder = new OrderUpdateTestDTO(
                newOrder.getId(),
                newOrder.getWarehouse(),
                newOrder.getTag(),
                newOrder.getDeliverDate(),
                OrderStatus.PENDING,
                new HashSet<>(Arrays.asList(savedItems))
        );

        //check if the inventoryService is not called when status is not changed to delivered
        this.restTemplate.put("/orders/" + newOrder.getId(), notDeliveredOrder, Order.class);
        verify(inventoryService, times(0))
                .updateInventory(new HashSet<>(Arrays.asList(savedItems)), newOrder.getWarehouse());


        //mock the order with a DTO, so that the restTemplate can serialize and deserialize the item list, with status changed to delivered
        OrderUpdateTestDTO orderUpdateTestDTO = new OrderUpdateTestDTO(
                newOrder.getId(),
                newOrder.getWarehouse(),
                newOrder.getTag(),
                newOrder.getDeliverDate(),
                OrderStatus.DELIVERED,
                new HashSet<>(Arrays.asList(savedItems))
        );

        this.restTemplate.put("/orders/" + newOrder.getId(), orderUpdateTestDTO, Order.class);

        //check if the inventoryService is called when status is changed to delivered
        verify(inventoryService, times(1))
                .updateInventory(new HashSet<>(Arrays.asList(savedItems)), newOrder.getWarehouse());
    }

    @Test
    public void updatingOrderSentCorrectErrors() {
        Order order = this.restTemplate.getForObject("/orders/1", Order.class);
        Item[] items = this.restTemplate.getForObject("/orders/1/items", Item[].class);
        //copy the order, so that the original order is not changed
        OrderUpdateTestDTO updatedOrder = new OrderUpdateTestDTO();
        updatedOrder.setId(order.getId());
        updatedOrder.setDeliverDate(order.getDeliverDate().minusYears(12)); //set deliver date to the past
        updatedOrder.setStatus(order.getStatus());
        updatedOrder.setTag("Updated tag");
        updatedOrder.setWarehouse(order.getWarehouse());
        updatedOrder.setItems(new HashSet<>(Arrays.asList(items)));

        //check if the deliver date is in the past sends a bad request
        ResponseEntity<BadRequestException> response = this.restTemplate.exchange(
                "/orders/1",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedOrder),
                BadRequestException.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getMessage(), "An order can not be delivered in the past!");

        updatedOrder.setDeliverDate(order.getDeliverDate().plusYears(12)); //set deliver date to the future
        updatedOrder.setId(2L); //set id to a different id

        //check if the id's in the path and body don't match sends a precondition failed
        ResponseEntity<PreConditionFailedException> response2 = this.restTemplate.exchange(
                "/orders/1",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedOrder),
                PreConditionFailedException.class
        );

        assertEquals(response2.getStatusCode(), HttpStatus.PRECONDITION_FAILED);
        assertEquals(response2.getBody().getMessage(), "The id's in the path and body don't match");


        updatedOrder.setId(order.getId()); //set id to the correct id
        updatedOrder.setTag(null);

        //check if a tag is empty sends a bad request
        response = this.restTemplate.exchange(
                "/orders/1",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedOrder),
                BadRequestException.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getMessage(), "The order tag should not be empty");

        updatedOrder.setTag("Updated tag");
        updatedOrder.getItems().stream().findFirst().get().setQuantity(0L);


        //check if the quantity of an item is less than 0 sends a bad request
        response = this.restTemplate.exchange(
                "/orders/1",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedOrder),
                BadRequestException.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getMessage(), "An item quantity can't be negative or 0!");

        //Make sure that status is set to delivered
        updatedOrder.setStatus(OrderStatus.DELIVERED);
        updatedOrder.getItems().stream().findFirst().get().setQuantity(10L);
        restTemplate.put("/orders/1", updatedOrder, Order.class);

        //check if the status of an order that is already delivered can't be changed
        updatedOrder.setStatus(OrderStatus.PENDING);
        response = this.restTemplate.exchange(
                "/orders/1",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedOrder),
                BadRequestException.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getMessage(), "You can't change the status of an order that is already delivered!");
    }

    @Test
    public void orderCanBeDeleted() {
        ResponseEntity<Order> response = this.restTemplate
                .exchange("/orders/2", org.springframework.http.HttpMethod.DELETE, null, Order.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteMappingSentCorrectErrors() {
        ResponseEntity<ResourceNotFoundException> response = this.restTemplate
                .exchange("/orders/0", org.springframework.http.HttpMethod.DELETE, null, ResourceNotFoundException.class);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody().getMessage(), "Cannot delete order with id: 0 Order not found");
    }

}
