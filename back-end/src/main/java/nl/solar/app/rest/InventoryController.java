package nl.solar.app.rest;

import com.fasterxml.jackson.annotation.JsonView;

import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller for all end-points directly affecting the resources.
 *
 * @author Julian Kruithof
 */
@RestController
@RequestMapping
public class InventoryController {

    @Autowired
    InventoryRepository inventoryRepo;

    @JsonView(ResourceView.Complete.class)
    @GetMapping("/inventory/test")
    public List<Inventory> inventory() {
        return inventoryRepo.findAll();
    }

    /**
     * Get a list of all resources and format it to the correct json string format
     * 
     * @return a json array of resources
     */
    @JsonView(ResourceView.Complete.class)
    @GetMapping(path = "/inventory", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getInventory() {
        List<Inventory> inventories = inventoryRepo.findAll();
        List<Map<String, Object>> formattedResources = new ArrayList<>();
        Map<Warehouse, List<Map<String, Object>>> GroupedByWarehouse = new HashMap<>();

        for (Inventory inventory : inventories) {
            Map<String, Object> productFormat = formatProductObject(inventory);

            // add product to a list of the warehouse of the current resources.
            GroupedByWarehouse.computeIfAbsent(inventory.getWarehouse(), k -> new ArrayList<>()).add(productFormat);
        }

        /*
         * Loop over all warehouses in the GroupBy map and format it to
         * [{
         * warehouse: {
         * id: Long,
         * name: String
         * },
         * products: [
         * {
         * id: Long,
         * productName: String,
         * description: String,
         * quantity: Int
         * },
         * ...
         * ]
         * },
         * ...
         * ]
         */
        for (Map.Entry<Warehouse, List<Map<String, Object>>> entry : GroupedByWarehouse.entrySet()) {
            Map<String, Object> formattedResource = new HashMap<>();
            formattedResource.put("warehouse", entry.getKey());
            formattedResource.put("products", entry.getValue());

            formattedResources.add(formattedResource);
        }
        return ResponseEntity.ok(formattedResources);
    }

    /**
     * Get all resources for a particular warehouse and format it to the correct
     * json format
     * 
     * @param id the id of a warehouse
     * @return a list of resources from a specific warehouse
     * @throws ResourceNotFoundException throw error if the specific warehouse
     *                                   doesn't exist
     */
    @GetMapping(path = "/warehouses/{id}/inventory", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getInventoryForWarehouse(@PathVariable long id)
            throws ResourceNotFoundException {
        List<Inventory> inventories = this.inventoryRepo.findInventoryForWarehouse(id);

        if (inventories.isEmpty()) {
            throw new ResourceNotFoundException("Warehouse doesn't exist");
        }

        List<Map<String, Object>> formattedProducts = new ArrayList<>();
        for (Inventory inventory : inventories) {
            formattedProducts.add(formatProductObject(inventory));
        }
        return ResponseEntity.ok(formattedProducts);
    }

    /**
     * Get a specific resource
     * 
     * @param wId the warehouse id
     * @param pId the product id
     * @return a resource in the correct format
     * @throws ResourceNotFoundException throw error if the resource doesn't exist
     */
    @GetMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Object> getSingleInventory(@PathVariable long wId, @PathVariable long pId)
            throws ResourceNotFoundException {
        Inventory inventory = this.inventoryRepo.findByIds(wId, pId);
        if (inventory == null) {
            throw new ResourceNotFoundException("The combination of warehouse and product doesn't return a resource");
        }

        return ResponseEntity.ok().body(formatProductObject(inventory));
    }

    /**
     * Update a specific resource
     * 
     * @param wId       the warehouse id
     * @param pId       the product id
     * @param inventory the updated version of a resource
     * @return a resource in the correct format
     * @throws PreConditionFailedException throw error if the warehouse id and
     *                                     product id in the body don't match the
     *                                     ids in the path
     */
    @JsonView(ResourceView.Complete.class)
    @PutMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Object> updateInventory(@PathVariable long wId, @PathVariable long pId,
            @RequestBody Inventory inventory)
            throws PreConditionFailedException {
        if (inventory.getProduct().getId() != pId || inventory.getWarehouse().getId() != wId) {
            throw new PreConditionFailedException(
                    "Ids of the body and path do not match");
        }

        Inventory update = this.inventoryRepo.save(inventory);
        return ResponseEntity.ok().body(update);
    }

    /**
     * Format a resource to an product object. This contains the product and the
     * quantity
     * making sure this is seen by the front-end as one object
     * 
     * @param inventory the resource being reformatted.
     * @return return a Map (object) of a product containing the quantity
     */
    private Map<String, Object> formatProductObject(Inventory inventory) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", inventory.getProduct().getId());
        map.put("productName", inventory.getProduct().getProductName());
        map.put("description", inventory.getProduct().getDescription());
        map.put("quantity", inventory.getQuantity());
        return map;
    }
}
