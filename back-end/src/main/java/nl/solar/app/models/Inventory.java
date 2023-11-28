package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private InventoryKey id = new InventoryKey();

    public InventoryKey getId() {
        return id;
    }

    public void setId(InventoryKey id) {
        this.id = id;
    }

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    @JsonIncludeProperties(value = {"id", "name"})
    @JsonManagedReference(value = "warehouse_inventory")
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonManagedReference(value = "product_inventory")
    private Product product;

    private int quantity;

    public static Inventory createDummyResource(Warehouse warehouse, Product product) {
        Inventory inventory = new Inventory();

        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity((int) (Math.floor(Math.random() * 40)));

        return inventory;
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
