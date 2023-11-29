package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import nl.solar.app.models.compositeKeys.InventoryKey;
import nl.solar.app.models.views.ResourceView;

import java.util.Objects;

/**
 * Representation of a resource of a certain product for a certain warehouse
 *
 * @author Julian
 */
@Entity
public class Inventory {

    @EmbeddedId
    @JsonIgnore
    private InventoryKey id = new InventoryKey();

    @ManyToOne()
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    @JsonIgnoreProperties(value = {"description"})
    @JsonView(ResourceView.Complete.class)
//    @JsonManagedReference(value = "warehouse_inventory")
    private Warehouse warehouse;

    @ManyToOne()
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonView(ResourceView.Complete.class)
//    @JsonManagedReference(value = "product_inventory")
    private Product product;

    @JsonView(ResourceView.Complete.class)
    private int quantity;

    public static Inventory createDummyResource(Warehouse warehouse, Product product) {
        Inventory inventory = new Inventory();

        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity((int) (Math.floor(Math.random() * 40)));

        return inventory;
    }

    public InventoryKey getId() {
        return id;
    }

    public void setId(InventoryKey id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Inventory inventory) {
            return inventory.getWarehouse().getId() == this.getWarehouse().getId() &&
                    inventory.getProduct().getId() == this.getProduct().getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId(), warehouse.getId());
    }
}
