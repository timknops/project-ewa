package nl.solar.app.models;



/**
 * Representation of a resource of a certain product for a certain warehouse
 *
 * @author Julian
 */
public class Resource {
    private Warehouse warehouse;
    private Product product;
    private int quantity;

    public Resource createDummyResource(Warehouse warehouse, Product product) {
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
}
