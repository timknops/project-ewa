package nl.solar.app.rest;

import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Product;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class productController {

    @Autowired
    EntityRepository<Product> productRepo;

    @GetMapping(produces = "application/json")
    public List<Product> getAll() {
        return this.productRepo.findALL();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) throws ResourceNotFoundException {
        Product product = this.productRepo.findById(id);

        if (product == null) {
            throw new ResourceNotFoundException("Product with id: " + id +  " was not found");
        }
        return ResponseEntity.ok(product);
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id) throws ResourceNotFoundException {
        Product deleted = this.productRepo.delete(id);

        if (deleted == null) {
            throw new ResourceNotFoundException("Cannot delete product with id: " + id + "\nProduct not found");
        }

        return ResponseEntity.ok(deleted);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product added = this.productRepo.save(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(added.getId()).toUri();
        return ResponseEntity.created(location).body(product);
    }

    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Product> updateProduct( @PathVariable long id, @RequestBody Product product) throws PreConditionFailedException {
        if (id != product.getId()) throw new PreConditionFailedException("Id of the body and path do not match");

        Product updated = this.productRepo.save(product);
        return ResponseEntity.ok(updated);
    }
}
