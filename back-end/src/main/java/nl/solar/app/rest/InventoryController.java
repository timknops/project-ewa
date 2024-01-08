package nl.solar.app.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.DTO.InventoryDTO;
import nl.solar.app.DTO.InventoryProductDTO;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Product;
import nl.solar.app.models.Warehouse;
import nl.solar.app.models.views.ResourceView;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for all end-points directly affecting the inventory.
 *
 * @author Julian Kruithof
 */
@RestController
@RequestMapping
public class InventoryController {

    @Autowired
    InventoryRepository inventoryRepo;

    @Autowired
    EntityRepository<Warehouse> warehouseRepo;

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
    public ResponseEntity<List<InventoryDTO>> getInventory() {
        List<InventoryDTO> inventory = new ArrayList<>();
        for (Warehouse warehouse : warehouseRepo.findAll()) {
            inventory.add(new InventoryDTO(warehouse, inventoryRepo.findInventoryForWarehouse(warehouse.getId())));
        }
        return ResponseEntity.ok(inventory);
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
    public ResponseEntity<List<InventoryProductDTO>> getInventoryForWarehouse(@PathVariable long id)
            throws ResourceNotFoundException {
        if (warehouseRepo.findById(id) == null) {
            throw new ResourceNotFoundException("Warehouse doesn't exist");
        }

        List<InventoryProductDTO> inventories = this.inventoryRepo.findInventoryForWarehouse(id);

        return ResponseEntity.ok(inventories);
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
    public ResponseEntity<InventoryProductDTO> getSingleInventory(@PathVariable long wId, @PathVariable long pId)
            throws ResourceNotFoundException {
        Inventory inventory = this.inventoryRepo.findByIds(pId, wId);
        if (inventory == null) {
            throw new ResourceNotFoundException("The combination of warehouse and product doesn't return a resource");
        }

        return ResponseEntity.ok().body(formatProductObject(inventory));
    }

    /**
     * Retrieves a list of products without inventory for a specific warehouse.
     *
     * @param wId the ID of the warehouse for which to retrieve products without
     *            inventory
     * @return a list of products without inventory for the specified warehouse
     */
    @GetMapping(path = "/warehouses/{wId}/products/without-inventory", produces = "application/json")
    public List<Product> getProductsWithoutInventory(@PathVariable long wId) {
        return this.inventoryRepo.findProductsWithoutInventory(wId);
    }

    /**
     * Update a specific resource
     *
     * @param wId              the warehouse id
     * @param pId              the product id
     * @param partiallyUpdated the updated version of a resource
     * @return a resource in the correct format
     * @throws BadRequestException       if the quantity is not set in the request
     *                                   body
     * @throws ResourceNotFoundException if the inventory that should be updated, is
     *                                   non-existent
     */
    @JsonView(ResourceView.Complete.class)
    @PatchMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Inventory> updateInventory(@PathVariable long wId, @PathVariable long pId,
            @RequestBody Map<String, Number> partiallyUpdated)
            throws ResourceNotFoundException, BadRequestException {
        Inventory existingInventory = inventoryRepo.findByIds(pId, wId);
        if (existingInventory == null) {
            throw new ResourceNotFoundException("inventory Not Found");
        }
        if (!partiallyUpdated.containsKey("quantity") || !partiallyUpdated.containsKey("minimum")) {
            throw new BadRequestException("Request body doesn't contain quantity");
        }

        existingInventory.setMinimum(partiallyUpdated.get("minimum").intValue());
        existingInventory.setQuantity(partiallyUpdated.get("quantity").longValue());

        Inventory update = this.inventoryRepo.save(existingInventory);

        return ResponseEntity.ok().body(update);
    }

    /**
     * Adds a new inventory item.
     *
     * @param inventory            the inventory item to be added, provided in the
     *                             request body
     * @param uriComponentsBuilder a builder for creating URI components, used to
     *                             build the location URI
     * @return a ResponseEntity with the added inventory item and the corresponding
     *         location URI
     * @throws BadRequestException if the product or warehouse is not set in the
     *                             inventory
     */
    @JsonView(ResourceView.Complete.class)
    @PostMapping(path = "/inventory", produces = "application/json")
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory,
            UriComponentsBuilder uriComponentsBuilder) throws BadRequestException {
        if (inventory.getProduct() == null) {
            throw new BadRequestException("Product is not set");
        }
        if (inventory.getWarehouse() == null) {
            throw new BadRequestException("Warehouse is not set");
        }

        Inventory newInventory = this.inventoryRepo.save(inventory);

        URI location = uriComponentsBuilder.path("/warehouses/{wId}/products/{pId}")
                .buildAndExpand(newInventory.getWarehouse().getId(), newInventory.getProduct().getId()).toUri();
        return ResponseEntity.created(location).body(newInventory);
    }

    /**
     * Format a resource to an product object. This contains the product and the
     * quantity
     * making sure this is seen by the front-end as one object
     *
     * @param inventory the resource being reformatted.
     * @return return a Map (object) of a product containing the quantity
     */
    private InventoryProductDTO formatProductObject(Inventory inventory) {
        return new InventoryProductDTO(inventory.getProduct().getId(),
                inventory.getProduct().getProductName(),
                inventory.getMinimum(),
                inventory.getQuantity());
    }
}
