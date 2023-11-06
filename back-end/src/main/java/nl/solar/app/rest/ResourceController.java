package nl.solar.app.rest;

import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Resource;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ResourceController {

    @Autowired
    ResourceRepository resourceRepo;

    @GetMapping(path = "/resources", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getResources() {
        List<Resource> resources = resourceRepo.findAll();
        List<Map<String, Object>> formattedResources = new ArrayList<>();
        Map<Warehouse, List<Map<String, Object>>> GroupedByWarehouse = new HashMap<>();

        for (Resource resource : resources) {
            Map<String, Object> productFormat = formatProductObject(resource);

            //add product to a list of the warehouse of the current resources.
            GroupedByWarehouse.computeIfAbsent(resource.getWarehouse(), k -> new ArrayList<>()).add(productFormat);
        }

        /*Loop over all warehouses in the GroupBy map and format it to
            [{
                warehouse: {
                    id: Long,
                    warehouseName: String
                },
                products: [
                    {
                        id: Long,
                        productName: String,
                        description: String,
                        quantity: Int
                    },
                    ...
                ]
            },
            ...
            ]
        */
        for (Map.Entry<Warehouse, List<Map<String, Object>>> entry : GroupedByWarehouse.entrySet()) {
            Map<String, Object> formattedResource = new HashMap<>();
            formattedResource.put("warehouse", entry.getKey());
            formattedResource.put("products", entry.getValue());

            formattedResources.add(formattedResource);
        }
        return ResponseEntity.ok(formattedResources);
    }

    @GetMapping(path = "/warehouses/{id}/resources", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getProductsForWarehouse(@PathVariable long id) {
        List<Resource> resources = this.resourceRepo.findResourceForWarehouse(id);

        if (resources.isEmpty()) {
            throw new BadRequestException("Warehouse doesn't exist");
        }

        List<Map<String, Object>> formattedProducts = new ArrayList<>();
        for (Resource resource : resources) {
            formattedProducts.add(formatProductObject(resource));
        }
        return ResponseEntity.ok(formattedProducts);
    }

    @GetMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Object> getResource(@PathVariable long wId, @PathVariable long pId) {
        Resource resource = this.resourceRepo.findResource(wId, pId);
        if (resource == null) {
            throw new ResourceNotFoundException("The combination of warehouse and product doesn't return a resource");
        }

        return ResponseEntity.ok().body(formatProductObject(resource));
    }

    @PutMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Object> updateResource(@PathVariable long wId, @PathVariable long pId, @RequestBody Resource resource) {
        if (resource.getProduct().getId() != pId || resource.getWarehouse().getId() != wId) {
            throw  new PreConditionFailedException(
                    "Ids of the body and path do not match"
            );
        }


        Resource update = this.resourceRepo.saveResource(resource);
        Map<String, Object> formatted = new HashMap<>();

        //store reference to warehouse so front-end knows for which warehouse a resource is updated.
        formatted.put("warehouse", update.getWarehouse());
        formatted.put("product", formatProductObject(update));
        return ResponseEntity.ok().body(formatted);
    }

    private Map<String, Object> formatProductObject(Resource resource) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", resource.getProduct().getId());
        map.put("productName", resource.getProduct().getProductName());
        map.put("description", resource.getProduct().getDescription());
        map.put("quantity", resource.getQuantity());
        return map;
    }
}
