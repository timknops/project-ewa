package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.models.utils.RandomDate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class which represents an order placed by Solar Sedum
 *
 * @author Julian Kruithof
 */
@Entity
public class Order {

    @Id
    @SequenceGenerator(name = "order_id_generator", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"id", "name"})
    private Warehouse warehouse;

    @JsonIgnore
    private LocalDateTime orderDate;

    private LocalDateTime deliverDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {

    }

    /**
     * Create a random order with different delivery dates, order dates and status.
     * @param warehouse warehouse for which a random order is created.
     * @return a random order
     */
    public static Order createDummyOrder(Warehouse warehouse) {
        final LocalDateTime MINIMUM_START = LocalDateTime.of(2023, 9, 12, 0, 0,0);
        final LocalDateTime MAXIMUM_END = LocalDateTime.of(2024, 2, 1, 0, 0,0);

        Order order = new Order();
        order.setId(0);
        order.setWarehouse(warehouse);
        order.orderDate = RandomDate.randomLocalDateTime(MINIMUM_START, MAXIMUM_END);
        order.deliverDate = RandomDate.randomLocalDateTime(order.orderDate, MAXIMUM_END);

        if (order.deliverDate.isBefore(LocalDateTime.now())) {
            order.orderStatus = OrderStatus.DELIVERED;
        } else {
            order.orderStatus = OrderStatus.PENDING;
        }
        return order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(LocalDateTime deliverDate) {
        this.deliverDate = deliverDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
