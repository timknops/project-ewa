package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.models.views.ResourceView;

/**
 * Data Transfer Object (DTO) representing a product, used to transfer product-related data
 * between the backend and frontend with specific view annotations.
 *
 * @author Julian Kruithof
 */
public class InventoryProductDTO {

    @JsonView(ResourceView.Complete.class)
    private Long id;

    @JsonView(ResourceView.Complete.class)
    private String productName;

    @JsonView(ResourceView.Complete.class)
    private String description;

    @JsonView(ResourceView.Complete.class)
    private Long quantity;

    /**
     * Constructs a ProductDTO with the specified product details.
     *
     * @param id           The unique identifier of the product.
     * @param productName  The name of the product.
     * @param description  The description of the product.
     * @param quantity     The quantity of the product.
     */
    public InventoryProductDTO(Long id, String productName, String description, Long quantity) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
    }
}
