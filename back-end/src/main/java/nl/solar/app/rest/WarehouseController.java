package nl.solar.app.rest;

import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {
    @Autowired
    EntityRepository<Warehouse> warehouseRepo;

    @Autowired
    InventoryRepository inventoryRepository;

    @GetMapping(produces = "application/json")
    public List<Warehouse> getAll(){
        return this.warehouseRepo.findAll();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) throws ResourceNotFoundException {
        Warehouse warehouse = this.warehouseRepo.findById(id);

        if (warehouse == null){
            throw new ResourceNotFoundException("Warehouse with id: " + id + " was not found");
        }

        return ResponseEntity.ok(warehouse);
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Warehouse> deleteWarehouseById(@PathVariable long id) throws ResourceNotFoundException {
        Warehouse warehouseToDelete = this.warehouseRepo.delete(id);

        if (warehouseToDelete == null){
            throw new ResourceNotFoundException("Cannot delete warehouse with id: " + id + "\nWarehouse not found");
        }

        try {
            this.inventoryRepository.deleteInventoryForWarehouse(warehouseToDelete);
        } catch (Exception ex) {
            //if exception is throw dont delete inventory, because of cascading rules
        }
        return ResponseEntity.ok(warehouseToDelete);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Warehouse> addOneWarehouse(@RequestBody Warehouse warehouse) throws BadRequestException {
        if (warehouse.getName() == null || warehouse.getName().isBlank()){
            throw new BadRequestException("Warehouse name can't be empty");
        }

        Warehouse newWarehouse = this.warehouseRepo.save(warehouse);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newWarehouse.getId()).toUri();
        return ResponseEntity.created(location).body(newWarehouse);
    }

    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable long id, @RequestBody Warehouse warehouse)
        throws PreConditionFailedException, BadRequestException {
        if (warehouse.getId() != id){
            throw new PreConditionFailedException("Id of the body and path do not match");
        }
        if (warehouse.getName() == null || warehouse.getName().isBlank()){
            throw new BadRequestException("Warehouse name can't be empty");
        }

        Warehouse updatedWarehouse = this.warehouseRepo.save(warehouse);
        return ResponseEntity.ok(updatedWarehouse);
    }
}
