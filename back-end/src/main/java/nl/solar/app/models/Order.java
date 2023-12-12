package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.models.utils.RandomDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Set<Item> items = new HashSet<>();

    @JsonIgnore
    private LocalDateTime orderDate;

    private String tag;

    private LocalDate deliverDate;


    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

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
        order.setTag(String.format("Ordered ... for %s", warehouse.getName()));
        order.setWarehouse(warehouse);
        order.orderDate = RandomDate.randomLocalDateTime(MINIMUM_START, LocalDateTime.now());
        order.deliverDate = RandomDate.randomLocalDate(order.orderDate.toLocalDate(), MAXIMUM_END.toLocalDate());

        if (order.deliverDate.isBefore(LocalDate.now())) {
            order.status = OrderStatus.DELIVERED;
        } else {
            order.status = OrderStatus.PENDING;
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

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
