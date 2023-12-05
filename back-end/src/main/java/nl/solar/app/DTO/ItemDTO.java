package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.solar.app.models.Product;

/**
 * Data Transfer Object (DTO) representing a item, used to transfer product-related data
 * between the backend and frontend with specific view annotations.
 *
 * @author Julian Kruithof
 */
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
