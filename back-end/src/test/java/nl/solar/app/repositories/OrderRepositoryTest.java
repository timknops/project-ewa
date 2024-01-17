package nl.solar.app.repositories;

import jakarta.persistence.Entity;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.models.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = { Repository.class, Entity.class }), showSql = false)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    CommandLineRunner commandLineRunner;

    private List<Order> orderList;

    @BeforeAll
    public static void init() {
        System.out.println("OrderRepositoryTest init");
    }


    @BeforeEach
    public void setup() throws Exception {
        commandLineRunner.run(null);
        orderList = orderRepository.findAll();
    }

    @Test
    public void FindAllShouldReturnAllOrders() {
        assertFalse(orderList.isEmpty(), "Order list should not be empty");
    }

    @Test
    public void findByIdReturnsCorrectOrder() {
        for (Order order : orderList) {
            Order foundOrder = orderRepository.findById(order.getId());
            assertEquals(order, foundOrder, "Order should be the same as the one found by id");
        }

        Order foundOrder = orderRepository.findById(0);
        assertNull(foundOrder, "Order should not be found");
    }

    @Test
    public void findOrdersForWarehouseReturnsCorrectOrders() {
        for (Order order : orderList) {
            List<Order> foundOrders = orderRepository.findOrdersWarehouse(order.getWarehouse().getId());
            for (Order foundOrder : foundOrders) {
                assertEquals(order.getWarehouse(), foundOrder.getWarehouse(), "Warehouse should be the same as the one passed in");
            }
        }
    }

    @Test
    public void findPendingOrdersReturnsCorrectOrders() {
        List<Order> foundOrders = orderRepository.findPendingOrders(orderList.get(0).getWarehouse());
        for (Order foundOrder : foundOrders) {
            assertEquals(orderList.get(0).getWarehouse(), foundOrder.getWarehouse(), "Warehouse should be the same as the one passed in");
            assertEquals(foundOrder.getStatus(), OrderStatus.PENDING, "Order should be pending");
        }
    }

    @Test
    public void deleteDeletesOrder() {
        Order order = orderList.get(0);
        Order deleted = orderRepository.delete(order.getId());
        assertEquals(order, deleted, "delete should return the deleted order");
        Order foundOrder = orderRepository.findById(order.getId());
        assertNull( foundOrder, "Order should be deleted");
    }

    @Test
    public void saveAddsOrUpdatesOrder() {
        Order existingOrder = orderList.get(0);
        Order newOrder = Order.createDummyOrder(existingOrder.getWarehouse());

        Order updated = orderRepository.save(existingOrder);
        Order existingOrderAfterUpdate = orderRepository.findById(existingOrder.getId());
        assertEquals(existingOrder, updated, "update should return the updated order");
        assertEquals(existingOrder, existingOrderAfterUpdate, "existing order should be updated in persistence context");

        Order newOrderBeforeAdd = orderRepository.findById(newOrder.getId());
        assertNull(newOrderBeforeAdd, "new order should not be in persistence context before the save");

        Order added = orderRepository.save(newOrder);
        Order newOrderAfterAdd = orderRepository.findById(newOrder.getId());
        assertEquals(newOrder, added, "add should return the added order");
        assertEquals(newOrder, newOrderAfterAdd, "new order should be added to persistence context");
    }

}
