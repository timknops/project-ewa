package nl.solar.app.service;

import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Item;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service for handling Business logic for the inventory
 * For example handling updating the inventory for orders, which are delivered,
 * This service is a layer between the controllers and the {@link nl.solar.app.repositories.InventoryRepository}
 *
 * @author Julian Kruithof
 */
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepo;

    /**
     * Update the inventory for A set of items of an order
     * The inventory products quantity will be incremented with the quantity of the item.
     * @param items the items delivered and for which the inventory should be updated.
     * @param warehouse the warehouse for which the inventory should be updated
     */
    public void updateInventory(Set<Item> items , Warehouse warehouse) throws BadRequestException {
        for (Item item : items) {
            //find the inventory to be updated
            Inventory inventory = inventoryRepo.findByIds(item.getProduct().getId(), warehouse.getId());
            inventory.setQuantity(inventory.getQuantity() + item.getQuantity()); //Update the quantity of the inventory
            inventoryRepo.save(inventory); // persist changes in database
        }
    }
}
