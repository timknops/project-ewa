package nl.solar.app.rest;

import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Product;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller of all products end-points
 *
 * @author Julian Kruithof
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    EntityRepository<Product> productRepo;
    @Autowired
    ResourceRepository resourceRepository;

    /**
     * get a list of all products
     * @return a list of products
     */
    @GetMapping(produces = "application/json")
    public List<Product> getAll() {
        return this.productRepo.findAll();
    }

    /**
     * get a specific product
     * @param id the id of the product
     * @return A product
     * @throws ResourceNotFoundException throw an error if the product with this id doesn't exist
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) throws ResourceNotFoundException {
        Product product = this.productRepo.findById(id);

        if (product == null) {
            throw new ResourceNotFoundException("Product with id: " + id + " was not found");
        }
        return ResponseEntity.ok(product);
    }

    /**
     * delete a product
     * @param id the id of the product to be deleted
     * @return the deleted product
     * @throws ResourceNotFoundException throw an error if the product with this id doesn't exist
     */
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id) throws ResourceNotFoundException {
        Product deleted = this.productRepo.delete(id);

        if (deleted == null) {
            throw new ResourceNotFoundException("Cannot delete product with id: " + id + "\nProduct not found");
        }

        //delete all resources for the product which was deleted
        this.resourceRepository.deleteResourcesForProduct(deleted);

        return ResponseEntity.ok(deleted);
    }

    /**
     * add a product
     * @param product the product to be added
     * @return the added product
     * @throws BadRequestException throw error if the name or description doesn't have a value.
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws BadRequestException {
        if (product.getProductName() == null || product.getProductName().isBlank()) throw new BadRequestException("Product name can't be empty");
        Product added = this.productRepo.save(product);

        //Add a resource to all warehouses for the certain product which was added
        this.resourceRepository.addResourcesForProduct(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(added.getId()).toUri();
        return ResponseEntity.created(location).body(product);
    }

    /**
     * update a resource
     * @param id the id of the product to be updated
     * @param product the updated product
     * @return the updated product
     * @throws PreConditionFailedException throw error if the id of the body and the path don't match
     * @throws BadRequestException throw error if the name or description doesn't have a value.
     */
    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody Product product)
            throws PreConditionFailedException, BadRequestException {
        if (id != product.getId()) throw new PreConditionFailedException("Id of the body and path do not match");
        if (product.getProductName() == null || product.getProductName().isBlank()) throw new BadRequestException("Product name can't be empty");

        Product updated = this.productRepo.save(product);
        return ResponseEntity.ok(updated);
    }
}
