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

import java.util.*;

/**
 * Controller for all end-points directly affecting the resources.
 *
 * @author Julian Kruithof
 */
@RestController
@RequestMapping
public class ResourceController {

    @Autowired
    ResourceRepository resourceRepo;

    @GetMapping("/resources/test")
    public List<Resource> resources() {
        return resourceRepo.findAll();
    }
    /**
     * Get a list of all resources and format it to the correct json string format
     * @return a json array of resources
     */
    @GetMapping(path = "/resources", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getResources() {
        List<Resource> resources = resourceRepo.findAll();
        System.out.println(resources);
        List<Map<String, Object>> formattedResources = new ArrayList<>();
        Map<Warehouse, List<Map<String, Object>>> GroupedByWarehouse = new HashMap<>();

        for (Resource resource : resources) {
            Map<String, Object> productFormat = formatProductObject(resource);

            //add product to a list of the warehouse of the current resources.
            GroupedByWarehouse.computeIfAbsent(resource.getWarehouse(), k -> new ArrayList<>()).add(productFormat);
        }
            System.out.println(GroupedByWarehouse);

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

    /**
     * Get all resources for a particular warehouse and format it to the correct json format
     * @param id the id of a warehouse
     * @return a list of resources from a specific warehouse
     * @throws ResourceNotFoundException throw error if the specific warehouse doesn't exist
     */
    @GetMapping(path = "/warehouses/{id}/resources", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getProductsForWarehouse(@PathVariable long id) throws ResourceNotFoundException {
        List<Resource> resources = this.resourceRepo.findResourceForWarehouse(id);

        if (resources.isEmpty()) {
            throw new ResourceNotFoundException("Warehouse doesn't exist");
        }

        List<Map<String, Object>> formattedProducts = new ArrayList<>();
        for (Resource resource : resources) {
            formattedProducts.add(formatProductObject(resource));
        }
        return ResponseEntity.ok(formattedProducts);
    }

    /**
     * Get a specific resource
     * @param wId the warehouse id
     * @param pId the product id
     * @return a resource in the correct format
     * @throws ResourceNotFoundException throw error if the resource doesn't exist
     */
    @GetMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Object> getResource(@PathVariable long wId, @PathVariable long pId) throws ResourceNotFoundException {
        Resource resource = this.resourceRepo.findResource(wId, pId);
        if (resource == null) {
            throw new ResourceNotFoundException("The combination of warehouse and product doesn't return a resource");
        }

        return ResponseEntity.ok().body(formatProductObject(resource));
    }

    /**
     * Update a specific resource
     * @param wId the warehouse id
     * @param pId the product id
     * @param resource the updated version of a resource
     * @return a resource in the correct format
     * @throws PreConditionFailedException throw error if the warehouse id and product id in the body don't match the ids in the path
     */
    @PutMapping(path = "/warehouses/{wId}/products/{pId}", produces = "application/json")
    public ResponseEntity<Object> updateResource(@PathVariable long wId, @PathVariable long pId, @RequestBody Resource resource)
    throws PreConditionFailedException {
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

    /**
     * Format a resource to an product object. This contains the product and the quantity
     * making sure this is seen by the front-end as one object
     * @param resource the resource being reformatted.
     * @return return a Map (object) of a product containing the quantity
     */
    private Map<String, Object> formatProductObject(Resource resource) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", resource.getProduct().getId());
        map.put("productName", resource.getProduct().getProductName());
        map.put("description", resource.getProduct().getDescription());
        map.put("quantity", resource.getQuantity());
        return map;
    }
}
