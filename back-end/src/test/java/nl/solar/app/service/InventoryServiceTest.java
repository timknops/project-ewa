package nl.solar.app.service;

import nl.solar.app.models.*;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link InventoryService}
 * This class tests the business logic of the inventory service
 *
 * @author Julian Kruithof
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private EntityRepository<Warehouse> warehouseRepository;

    @Autowired
    private EntityRepository<Product> productRepository;

    @Test
    public void updateInventoryUpdatesInventory() {
        Warehouse warehouse = warehouseRepository.findById(1000);
        Product product = productRepository.findById(1);
        Product product2 = productRepository.findById(2);

        Inventory originalInventory = inventoryRepository.findByIds(product.getId(), warehouse.getId());
        Inventory originalInventory2 = inventoryRepository.findByIds(product2.getId(), warehouse.getId());

        Order order = Order.createDummyOrder(warehouse);
        Set<Item> items = new HashSet<>();
        items.add(new Item(product, order, 10));
        items.add(new Item(product2, order, 20));

        inventoryService.updateInventory(items, warehouse);

        Inventory updatedInventory = inventoryRepository.findByIds(product.getId(), warehouse.getId());
        Inventory updatedInventory2 = inventoryRepository.findByIds(product2.getId(), warehouse.getId());

        assertEquals(originalInventory.getQuantity() + 10, updatedInventory.getQuantity());
        assertEquals(originalInventory2.getQuantity() + 20, updatedInventory2.getQuantity());
    }
}
