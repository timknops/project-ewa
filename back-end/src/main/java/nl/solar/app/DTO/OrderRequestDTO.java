package nl.solar.app.DTO;

import nl.solar.app.models.Order;

import java.util.List;

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
