package nl.solar.app.models;

import nl.solar.app.DTO.InventoryProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Warehouse warehouse, warehouse2;
    Product product;

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse(1, "SolarSedum", "Amsterdam");
        warehouse2 = new Warehouse(2, "Superzon", "Amsterdam");

        product = new Product();
        product.setId(1);
        product.setProductName("Solar Panel");
        product.setDescription("A solar panel");;
    }

    @Test
    public void createDummyInventoryCorrectlyCreatesInventory() {
        // Arrange
        int minimumQuantity = 0;
        int minimumMinimum = 10;
        int maximumQuantityAndMinimum = 40;

        // Act
        Inventory inventory = Inventory.createDummyInventory(warehouse, product);
        Inventory inventory2 = Inventory.createDummyInventory(warehouse2, product);

        // Assert
        assertEquals(warehouse, inventory.getWarehouse(), "created warehouse should be the same as the warehouse in the inventory");
        assertEquals(product, inventory.getProduct(), "created product should be the same as the product in the inventory");
        assertTrue(inventory.getQuantity() >= minimumQuantity && inventory.getQuantity() <= maximumQuantityAndMinimum, "created quantity should be between 0 and 40");
        assertTrue(inventory.getMinimum() >= minimumMinimum && inventory.getMinimum() <= maximumQuantityAndMinimum, "created minimum should be between 10 and 40");

        assertEquals(warehouse2, inventory2.getWarehouse(), "created warehouse should be the same as the warehouse in the inventory");
        assertEquals(product, inventory2.getProduct(), "created product should be the same as the product in the inventory");
        assertTrue(inventory2.getQuantity() >= minimumQuantity && inventory2.getQuantity() <= maximumQuantityAndMinimum, "created quantity should be between 0 and 40");
        assertTrue(inventory2.getMinimum() >= minimumMinimum && inventory2.getMinimum() <= maximumQuantityAndMinimum, "created minimum should be between 10 and 40");
    }

    @Test
    public void formatInventoryToDTOCorrectlyFormatsInventory() {
        // Arrange
        Warehouse warehouse = new Warehouse(1, "SolarSedum", "Amsterdam");
        Product product = new Product();
        product.setId(1);
        product.setProductName("Solar Panel");
        product.setDescription("A solar panel");
        Inventory inventory = Inventory.createDummyInventory(warehouse, product);

        // Act
        InventoryProductDTO inventoryProductDTO = inventory.formatInventoryToDTO();

        // Assert
        assertEquals(inventory.getProduct().getId(), inventoryProductDTO.getId(), "product id should be the same as the product id in the inventory");
        assertEquals(inventory.getProduct().getProductName(), inventoryProductDTO.getProductName(), "product name should be the same as the product name in the inventory");
        assertEquals(inventory.getMinimum(), inventoryProductDTO.getMinimum(), "minimum should be the same as the minimum in the inventory");
        assertEquals(inventory.getQuantity(), inventoryProductDTO.getQuantity(), "quantity should be the same as the quantity in the inventory");
    }

    @Test
    public void InventoriesAreEqualIfProductAndWarehouseAreTheSame() {
        // Arrange
        Inventory inventory = Inventory.createDummyInventory(warehouse, product);
        Inventory inventory2 = Inventory.createDummyInventory(warehouse, product);
        Inventory inventory3 = Inventory.createDummyInventory(warehouse2, product);
        Inventory inventory4 = Inventory.createDummyInventory(warehouse, new Product());

        // Act
        boolean inventoriesAreEqual = inventory.equals(inventory2);
        boolean inventoriesAreNotEqual = inventory.equals(inventory3);
        boolean inventoriesAreNotEqual2 = inventory.equals(inventory4);


        // Assert
        assertTrue(inventoriesAreEqual, "inventories should be equal if the product and warehouse are the same");
        assertFalse(inventoriesAreNotEqual, "inventories should not be equal if the product and warehouse are not the same");
        assertFalse(inventoriesAreNotEqual2, "inventories should not be equal if the product and warehouse are not the same");
    }
}
