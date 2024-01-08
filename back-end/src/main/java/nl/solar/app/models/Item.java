package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import nl.solar.app.models.compositeKeys.ItemKey;

import java.util.Objects;

/**
 * Class which represents all products which are ordered at once, via a {@link  Order}
 *
 * @author Julian Kruithof
 */
@Entity
@Table(name = "item")
public class Item {

    @EmbeddedId
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ItemKey compositeId = new ItemKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonIncludeProperties({"id", "productName"})
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonIncludeProperties({"id"})
    private Order order;

    private long quantity;

    public Item(Product product, Order order, long quantity) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        ItemKey key = new ItemKey();
        key.setProductId(product.getId());
        key.setOrderId(order.getId());
        this.compositeId = key;
    }


    public Item() {}

    public ItemKey getCompositeId() {
        return compositeId;
    }

    public void setCompositeId(ItemKey compositeId) {
        this.compositeId = compositeId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.compositeId.setProductId(this.product.getId());
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.compositeId.setOrderId(this.order.getId());
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(getProduct(), item.getProduct()) && Objects.equals(getOrder(), item.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getOrder());
    }
}
