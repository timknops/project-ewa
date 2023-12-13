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

    /**
     * Get a list of all orders found
     * @return list of orders
     */
    @GetMapping(produces = "application/json")
    public List<Order> getAll() {
        return this.orderRepo.findAll();
    }

    /**
     * Get a order with a certain id
     * @param id the id of an order
     * @return a order object
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Order> getOrder(@PathVariable long id) {
        Order order = this.orderRepo.findById(id);

        if (order == null) {
            throw new ResourceNotFoundException("Order with id: " + id + " was not found");
        }

        return ResponseEntity.ok(order);
    }

    /**
     * Get all items for one particular order
     * An item is a product, sold by solar sedum
     * @param id the id of an order
     * @return a list of items
     */
    @GetMapping(path = "{id}/items", produces = "application/json")
    public List<Item> getItemsForOrder(@PathVariable long id) {
        return this.itemRepo.getItemsForOrder(id);
    }

    /**
     * Add an order and the items belonging to the order to the database.
     * @param Request a request received from the backend. Since the order id isn't known beforehand, the list of
     *                items is sent in differently than normal to the back-end. <br> see {@link OrderRequestDTO}
     *
     * @return The order which is added
     */
    @Transactional
    @PostMapping(produces = "application/json")
    public ResponseEntity<Order> addOrder(@RequestBody OrderRequestDTO Request) {
        //set the order date to the current datetime
        Request.getOrder().setOrderDate(LocalDateTime.now().withNano(0));
        Request.getOrder().setStatus(OrderStatus.PENDING);

        if (Request.getOrder().getWarehouse() == null) {
            throw new BadRequestException("An order should be placed for a warehouse!");
        }

        if (Request.getOrder().getDeliverDate().isBefore(Request.getOrder().getOrderDate().toLocalDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }

        Order savedOrder = this.orderRepo.save(Request.getOrder());

        //add all the items and manage bidirectional relationships
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

    /**
     * Update the given order and the items which belongs to this order.
     * @param id the id of the order which is being updated
     * @param order the data of an order which is being updated
     * @return the updated order
     */
    @Transactional
    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Order> updateOrder(@PathVariable long id, @RequestBody Order order) {
        if (order.getId() != id) {
            throw new PreConditionFailedException("The id's in the path and body don't match");
        }

        //find the existing order to handle some change checks.
        Order existingOrder = this.orderRepo.findById(id);

        if (order.getDeliverDate().isBefore(existingOrder.getOrderDate().toLocalDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }
        order.setOrderDate(existingOrder.getOrderDate());

        //TODO handle updating the inventory for the given items

        Order updated = this.orderRepo.save(order);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an order
     * @param id the id of the order which is deleted
     * @return the deleted order
     */
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Order> deleteOrder(@PathVariable long id) {
        Order toBeDelete = this.orderRepo.delete(id);

        if (toBeDelete == null) {
            throw new ResourceNotFoundException("Cannot delete order with id: " + id + "Order not found");
        }
        return ResponseEntity.ok(toBeDelete);
    }
}
