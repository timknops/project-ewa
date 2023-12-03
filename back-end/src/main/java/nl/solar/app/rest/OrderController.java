package nl.solar.app.rest;

import nl.solar.app.enums.OrderStatus;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Order;
import nl.solar.app.repositories.EntityRepository;
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

    @PostMapping(produces = "application/json")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        //set the order date to the current datetime
        order.setOrderDate(LocalDateTime.now().withNano(0));
        order.setOrderStatus(OrderStatus.PENDING);

        if (order.getWarehouse() == null) {
            throw new BadRequestException("An order should be placed for a warehouse!");
        }

        if (order.getDeliverDate().isBefore(order.getOrderDate())) {
            throw new BadRequestException("An order can not be delivered in the past!");
        }

        Order savedOrder = this.orderRepo.save(order);

        //TODO handle adding items to order

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedOrder.getId()).toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }

}
