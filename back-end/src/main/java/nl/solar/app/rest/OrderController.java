package nl.solar.app.rest;

import jakarta.transaction.Transactional;
import nl.solar.app.DTO.ItemDTO;
import nl.solar.app.DTO.OrderRequestDTO;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Item;
import nl.solar.app.models.Order;
import nl.solar.app.models.Product;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for all endpoint affecting the order entity
 *
 * @author Julian Kruithof
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    EntityRepository<Product> productRepo;
    @Autowired
    ItemRepository itemRepo;
    @Autowired
    private EntityRepository<Order> orderRepo;

    @GetMapping(produces = "application/json")
    public List<Order> getAll() {
        return this.orderRepo.findAll();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Order> getOrder(@PathVariable long id) {
        Order order = this.orderRepo.findById(id);

        if (order == null) {
            throw new ResourceNotFoundException("Order with id: " + id + " was not found");
        }

        return ResponseEntity.ok(order);
    }

    @Transactional
    @PostMapping(produces = "application/json")
    public ResponseEntity<Order> addOrder(@RequestBody OrderRequestDTO Request) {
        //set the order date to the current datetime
        Request.getOrder().setOrderDate(LocalDateTime.now().withNano(0));
        Request.getOrder().setOrderStatus(OrderStatus.PENDING);

        if (Request.getOrder().getWarehouse() == null) {
            throw new BadRequestException("An order should be placed for a warehouse!");
        }

        if (Request.getOrder().getDeliverDate().isBefore(Request.getOrder().getOrderDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }

        Order savedOrder = this.orderRepo.save(Request.getOrder());

        for (ItemDTO item : Request.getItems()) {
            Product product = productRepo.findById(item.getProduct().getId()); // find the correct persisted product.
            //create the bidirectional relationships
            Item newItem = new Item();
            newItem.setProduct(product);
            newItem.setOrder(savedOrder);
            newItem.setQuantity(item.getQuantity());

            product.getItems().add(newItem);
            savedOrder.getItems().add(newItem);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedOrder.getId()).toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }

    @Transactional
    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Order> updateOrder(@PathVariable long id, @RequestBody OrderRequestDTO requestDTO) {
        if (requestDTO.getOrder().getId() != id) {
            throw new PreConditionFailedException("The id's in the path and body don't match");
        }

        Order existingOrder = this.orderRepo.findById(id);
        if (requestDTO.getOrder().getDeliverDate().isBefore(existingOrder.getOrderDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }
        requestDTO.getOrder().setOrderDate(existingOrder.getOrderDate());
        Order updated = this.orderRepo.save(requestDTO.getOrder());

        return ResponseEntity.ok(updated);
    }


}
