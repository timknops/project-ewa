package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.solar.app.models.Product;

public class ItemDTO {
    @JsonIncludeProperties({"id", "productName"})
    private Product product;
    private Long quantity;

    public ItemDTO() {
    }

    public ItemDTO(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

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
