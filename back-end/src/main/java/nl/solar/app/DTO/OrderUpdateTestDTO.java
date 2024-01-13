package nl.solar.app.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.models.Item;
import nl.solar.app.models.Warehouse;

import java.time.LocalDate;
import java.util.Set;

/**
 * Since the item list in the order is set to write only
 * the restTemplate can't deserialize the item list for testing despite the front-end being able to do so.
 * This class is only used for testing purposes.
 * it mocks a normal order Object without jackson annotations.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderUpdateTestDTO {
    private long id;
    private Warehouse warehouse;
    private String tag;
    private LocalDate deliverDate;
    private OrderStatus status;
    private Set<Item> items;

    public OrderUpdateTestDTO(long id, Warehouse warehouse, String tag, LocalDate deliverDate, OrderStatus status, Set<Item> items) {
        this.id = id;
        this.warehouse = warehouse;
        this.tag = tag;
        this.deliverDate = deliverDate;
        this.status = status;
        this.items = items;
    }
}
