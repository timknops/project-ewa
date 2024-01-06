package nl.solar.app.repositories;

import jakarta.persistence.Entity;
import nl.solar.app.DTO.InventoryProductDTO;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Product;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.TestPropertySource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = { Repository.class, Entity.class }), showSql = false)
public class InventoryRepositoryTest {

    @Autowired
    CommandLineRunner commandLineRunner;

    @Qualifier("INVENTORY.JPA")
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private EntityRepository<Warehouse> warehouseRepository;

    private List<Inventory> inventoryList;

    @BeforeAll
    public static void init() {
        System.out.println("InventoryRepositoryTest init");
    }

    @BeforeEach
    public void setup() throws Exception {
        commandLineRunner.run(null);
        inventoryList = inventoryRepository.findAll();
    }

    @Test
    public void FindAllReturnsInventory() {
        assertFalse(inventoryList.isEmpty(), "inventory list should not be empty");
    }

    @Test
    public void FindByIdReturnsInventory() {
        for (Inventory inventory : inventoryList) {
            Inventory inventory1 = inventoryRepository.findByIds(inventory.getProduct().getId(), inventory.getWarehouse().getId());
            //find by id should return the same inventory as the one in the list
            assertEquals(inventory.getProduct(), inventory1.getProduct(), "Product should be the same");
            assertEquals(inventory.getWarehouse(), inventory1.getWarehouse(), "Warehouse should be the same");
            assertEquals(inventory.getQuantity(), inventory1.getQuantity(), "Quantity should be the same");
            assertEquals(inventory.getMinimum(), inventory1.getMinimum(), "Minimum should be the same");
        }
    }

    @Test
    public void FindByIdReturnsNullInCorrectSituations() {
        Inventory inventory = inventoryRepository.findByIds(0, 0); //should return null because there is no inventory with product id 0 and warehouse id 0
        Inventory inventory1 = inventoryRepository.findByIds(0, inventoryList.get(0).getWarehouse().getId());
        Inventory inventory2 = inventoryRepository.findByIds(inventoryList.get(0).getProduct().getId(), 0);

        assertNull(inventory, "inventory should be null");
        assertNull(inventory1, "If the warehouse id exists but the product id doesn't, inventory should be null");
        assertNull(inventory2, "If the product id exists but the warehouse id doesn't, inventory should be null");
    }

    @Test
    public void FindInventoryForWarehouseReturnsCorrectInventory() {
        List<InventoryProductDTO> inventoryForWarehouse = inventoryRepository.findInventoryForWarehouse(inventoryList.get(0).getWarehouse().getId());

        //find all inventory for the first warehouse from the total list
        List<InventoryProductDTO> inventoryForWarehouseFromTotalList =
                inventoryList.stream().filter(inventory -> inventory.getWarehouse().getId() == inventoryList.get(0).getWarehouse().getId())
                        .map(inventory -> new InventoryProductDTO(inventory.getProduct().getId(), inventory.getProduct().getProductName(), inventory.getMinimum(), inventory.getQuantity()))
                        .toList();


       assertFalse(inventoryForWarehouse.isEmpty(), "inventoryForWarehouse should not be empty");
       assertEquals(inventoryForWarehouse.size(), inventoryForWarehouseFromTotalList.size(), "inventoryForWarehouse should be the same size as inventoryList with removed inventory from other warehouses");
       assertThat(inventoryForWarehouse, containsInAnyOrder(inventoryForWarehouseFromTotalList.toArray()));
       //inventoryForWarehouse should be smaller than inventoryList since other warehouses also have inventory
    }

    @Test
    public void GetProductWithoutInventoryReturnsCorrectList() {
        List<Product> productWithoutInventory = inventoryRepository.findProductsWithoutInventory(inventoryList.get(0).getWarehouse().getId());
        //commandLineRunner should have added inventory for all products, so productWithoutInventory should be empty
        assertTrue(productWithoutInventory.isEmpty(), "productWithoutInventory should be empty when all products have inventory");

        inventoryRepository.deleteByIds(inventoryList.get(0).getProduct().getId(), inventoryList.get(0).getWarehouse().getId());

        productWithoutInventory = inventoryRepository.findProductsWithoutInventory(inventoryList.get(0).getWarehouse().getId());
        //after deleting the inventory for the first product, productWithoutInventory should contain 1 product
        assertFalse(productWithoutInventory.isEmpty(), "productWithoutInventory should not be empty");
        assertThat(productWithoutInventory.size(), is(1));
        assertEquals(productWithoutInventory.get(0), inventoryList.get(0).getProduct(), "productWithoutInventory should contain the product which was deleted");
    }

    @Test
    public void DeleteByIdsDeletesInventory() {
        Inventory inventory = inventoryList.get(0);
        Inventory deleted = inventoryRepository.deleteByIds(inventory.getProduct().getId(), inventory.getWarehouse().getId());
        Inventory deletedNonExisting = inventoryRepository.deleteByIds(0, 0);

        Inventory inventory1 = inventoryRepository.findByIds(inventory.getProduct().getId(), inventory.getWarehouse().getId());

        assertEquals(inventory, deleted, "deleted inventory should be the same as the one that was deleted");
        assertNull(deletedNonExisting, "if the inventory doesn't exist, deleted should be null");
        assertNull(inventory1, "inventory should be null");
    }

    @Test
    public void SaveAddsOrUpdatesInventory() {
        Inventory inventory = inventoryList.get(0);
        inventory.setQuantity(100);
        inventory.setMinimum(10);

        Inventory saved = inventoryRepository.save(inventory);
        Inventory inventory1 = inventoryRepository.findByIds(inventory.getProduct().getId(), inventory.getWarehouse().getId());

        assertEquals(inventory, saved, "saved inventory should be the same as the one that was saved");
        assertEquals(inventory, inventory1, "inventory should be the same as the one that was saved");

        //save should also work for new inventory
        Warehouse warehouse = new Warehouse(0, "new warehouse", "wibautstraat 80, 1091GP, Amsterdam");
        Warehouse savedWarehouse = warehouseRepository.save(warehouse); //save warehouse so that it exists in the database, needed to create a new inventory item in the database
        Inventory inventory2 = Inventory.createDummyInventory(savedWarehouse, inventory.getProduct());
        Inventory saved2 = inventoryRepository.save(inventory2);
        Inventory inventory3 = inventoryRepository.findByIds(inventory2.getProduct().getId(), inventory2.getWarehouse().getId());

        assertEquals(inventory2, saved2, "saved inventory should be the same as the one that was saved");
        assertEquals(inventory2, inventory3, "inventory should be the same as the one that was saved");

    }

}
