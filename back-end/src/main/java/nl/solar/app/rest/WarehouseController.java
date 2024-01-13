package nl.solar.app.rest;

import jakarta.transaction.Transactional;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceConflictException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Order;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller for all endpoint affecting the warehouse entity
 *
 * @author Wilco van de Pol
 */
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {
    @Autowired
    EntityRepository<Warehouse> warehouseRepo;

    @Autowired
    OrderRepository orderRepo;

    /**
     * Get a list of all warehouses
     * @return List of warehouses
     */
    @GetMapping(produces = "application/json")
    public List<Warehouse> getAll() {
        return this.warehouseRepo.findAll();
    }

    /**
     * Get a warehouse by a specific id
     * @param id The id of the warehouse
     * @return A warehouse
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) throws ResourceNotFoundException {
        Warehouse warehouse = this.warehouseRepo.findById(id);

        if (warehouse == null) {
            throw new ResourceNotFoundException("Warehouse with id: " + id + " was not found");
        }

        return ResponseEntity.ok(warehouse);
    }

    /**
     * Get orders that are for a specific warehouse
     * @param id The id of the warehouse
     * @return List with orders for that specific warehouse
     */
    @GetMapping(path = "{id}/orders", produces = "application/json")
    public List<Order> getOrdersForWarehouse(@PathVariable long id) {
        Warehouse warehouse = this.warehouseRepo.findById(id);

        if (warehouse == null) {
            throw new ResourceNotFoundException("Can't find orders because warehouse doesn't exist");
        }
        return this.orderRepo.findOrdersWarehouse(id);
    }

    /**
     * Delete a warehouse
     * @param id The id of the warehouse
     * @return The deleted warehouse
     */
    @Transactional
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Warehouse> deleteWarehouseById(@PathVariable long id)
            throws ResourceNotFoundException, ResourceConflictException {
        Warehouse warehouseToDelete = this.warehouseRepo.findById(id);

        if (warehouseToDelete == null) {
            throw new ResourceNotFoundException("Cannot delete warehouse with id: " + id + "\nWarehouse not found");
        }

        // Check if warehouse has pending orders
        if (!orderRepo.findPendingOrders(warehouseToDelete).isEmpty()) {
            throw new ResourceConflictException(
                    "Cannot delete warehouse with id: " + id + "\nWarehouse has pending orders");
        }

        // Remove warehouse from orders
        for (Order order : warehouseToDelete.getOrders()) {
            order.setWarehouse(null);
        }
        Warehouse deletedWarehouse = this.warehouseRepo.delete(id);
        return ResponseEntity.ok(deletedWarehouse);
    }

    /**
     * Create a new warehouse
     * @param warehouse The warehouse to add
     * @return The new warehouse
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<Warehouse> addOneWarehouse(@RequestBody Warehouse warehouse) throws BadRequestException {
        if (warehouse.getName() == null || warehouse.getName().isBlank()) {
            throw new BadRequestException("Warehouse name can't be empty");
        }

        Warehouse newWarehouse = this.warehouseRepo.save(warehouse);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newWarehouse.getId()).toUri();
        return ResponseEntity.created(location).body(newWarehouse);
    }

    /**
     * Update the given warehouse
     * @param id The id of the oder to update
     * @param warehouse The warehouse changes to update
     * @return The updated warehouse
     */
    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable long id, @RequestBody Warehouse warehouse)
            throws PreConditionFailedException, BadRequestException {
        if (warehouse.getId() != id) {
            throw new PreConditionFailedException("Id of the body and path do not match");
        }
        if (warehouse.getName() == null || warehouse.getName().isBlank()) {
            throw new BadRequestException("Warehouse name can't be empty");
        }

        Warehouse updatedWarehouse = this.warehouseRepo.save(warehouse);
        return ResponseEntity.ok(updatedWarehouse);
    }
}
