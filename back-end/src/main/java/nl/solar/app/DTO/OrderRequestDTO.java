package nl.solar.app.DTO;

import nl.solar.app.models.Order;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the object received from the front-end when adding an order
 * Since the order-id isn't set yet, the items list, needs to be deserialized in a different way than normal.
 *
 * @author Julian Kruithof
 */
public class OrderRequestDTO {

    private Order order;

    private List<ItemDTO> items;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
