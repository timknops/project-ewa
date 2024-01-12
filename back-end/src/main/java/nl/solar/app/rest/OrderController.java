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
import nl.solar.app.repositories.OrderRepository;
import nl.solar.app.service.InventoryService;
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
    private EntityRepository<Product> productRepo;
    @Autowired
    private ItemRepository itemRepo;
    @Autowired
    private EntityRepository<Order> orderRepo;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Get a list of all orders found
     * @return list of orders
     */
    @GetMapping(produces = "application/json")
    public List<Order> getAll() {
        return this.orderRepo.findAll();
    }

    /**
     * Get a list of all pending orders found
     * @return list of orders
     */
    @GetMapping(path = "pending")
    public List<Order> getAllPending() {
        return this.orderRepository.findAllPendingOrders();
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
     * @param order the order which is added.
     *
     * @return The order which is added
     */
    @Transactional
    @PostMapping(produces = "application/json")
    public ResponseEntity<Order> addOrder(@RequestBody OrderRequestDTO order) {
        Order newOrder = new Order();
        //set the order date to the current datetime
        newOrder.setOrderDate(LocalDateTime.now().withNano(0));
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setDeliverDate(order.getDeliverDate());
        newOrder.setTag(order.getTag());
        newOrder.setWarehouse(order.getWarehouse());

        if (order.getWarehouse() == null) {
            throw new BadRequestException("An order should be placed for a warehouse!");
        }

        if (order.getDeliverDate().isBefore(newOrder.getOrderDate().toLocalDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }
        Order savedOrder = this.orderRepo.save(newOrder);

        //add all the items and manage bidirectional relationships
        for (ItemDTO item : order.getItems()) {
            Item newItem = new Item(item.getProduct(), newOrder, item.getQuantity());
            item.getProduct().getItems().add(newItem);
            newOrder.getItems().add(newItem);
            this.itemRepo.save(newItem);
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

        //check if a tag is empty
        if (order.getTag() == null) {
            throw new BadRequestException("The order tag should not be empty");
        }

        if (order.getItems().stream().anyMatch(item -> item.getQuantity() <= 0)) {
            throw new BadRequestException("An item quantity can't be negative or 0!");
        }

        //find the existing order to handle some change checks.
        Order existingOrder = this.orderRepo.findById(id);

        if (existingOrder.getStatus() == OrderStatus.DELIVERED && order.getStatus() != OrderStatus.DELIVERED) {
            throw new BadRequestException("You can't change the status of an order that is already delivered!");
        }

        if (order.getDeliverDate().isBefore(existingOrder.getOrderDate().toLocalDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }
        order.setOrderDate(existingOrder.getOrderDate());

        //If the new order is set to delivered update the Inventory
        if (existingOrder.getStatus() == OrderStatus.PENDING && order.getStatus() == OrderStatus.DELIVERED) {
            System.out.println("Updating inventory");
            this.inventoryService.updateInventory(order.getItems(), order.getWarehouse());
        }

        Order updated = this.orderRepo.save(order);
        return ResponseEntity.ok(updated);
    }

    /**
     * Update the status of an order with a specific id from PENDING to DELIVERED
     * @param id The id of the order to update
     * @return The updated order
     */
    @PatchMapping(path = "{id}/status", produces = "application/json")
    public ResponseEntity<Order> updateOrderStatusOnly(@PathVariable long id){
        //find the existing order to handle some change checks.
        Order existingOrder = this.orderRepo.findById(id);

        if (existingOrder == null) {
            throw new BadRequestException("The order should exist");
        }

        if (existingOrder.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("You can't change the status of an order that is already delivered ");
        }

        //If the new order is set to delivered update the Inventory
        if (existingOrder.getStatus() == OrderStatus.PENDING) {
            this.inventoryService.updateInventory(existingOrder.getItems(), existingOrder.getWarehouse());
            existingOrder.setStatus(OrderStatus.DELIVERED);
        }

        Order updated = this.orderRepo.save(existingOrder);
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
            throw new ResourceNotFoundException("Cannot delete order with id: " + id + " Order not found");
        }
        return ResponseEntity.ok(toBeDelete);
    }
}
