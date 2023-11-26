package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonView;

import nl.solar.app.models.views.ResourceView;

import java.util.Objects;

/**
 * Representation of a resource of a certain product for a certain warehouse
 *
 * @author Julian
 */
public class Resource {

    @JsonView(ResourceView.Complete.class)
    private Warehouse warehouse;
    @JsonView(ResourceView.Complete.class)
    private Product product;
    @JsonView(ResourceView.Complete.class)
    private int quantity;

    public static Resource createDummyResource(Warehouse warehouse, Product product) {
        Resource resource = new Resource();

        resource.setProduct(product);
        resource.setWarehouse(warehouse);
        resource.setQuantity((int) (Math.floor(Math.random() * 40)));

        return resource;
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
        if (obj instanceof Resource resource) {
            return resource.getWarehouse().getId() == this.getWarehouse().getId() &&
                    resource.getProduct().getId() == this.getProduct().getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId(), warehouse.getId());
    }
}
