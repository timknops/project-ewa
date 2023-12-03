package nl.solar.app.DTO;

import nl.solar.app.models.Product;

public class ItemDTO {
    private Product product;
    private Long quantity;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
