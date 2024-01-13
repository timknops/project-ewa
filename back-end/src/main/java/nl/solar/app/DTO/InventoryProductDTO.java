package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.solar.app.models.views.ResourceView;

/**
 * Data Transfer Object (DTO) representing a product, used to transfer product-related data
 * between the backend and frontend with specific view annotations.
 *
 * @author Julian Kruithof
 */
@Getter
@Setter
@EqualsAndHashCode
public class InventoryProductDTO {

    @JsonView(ResourceView.Complete.class)
    private Long id;

    @JsonView(ResourceView.Complete.class)
    private String productName;

    @JsonView(ResourceView.Complete.class)
    private Integer minimum;

    @JsonView(ResourceView.Complete.class)
    private Long quantity;

    /**
     * Constructs a ProductDTO with the specified product details.
     *
     * @param id           The unique identifier of the product.
     * @param productName  The name of the product.
     * @param minimum      The minimum of the inventory.
     * @param quantity     The quantity of the inventory.
     */
    public InventoryProductDTO(Long id, String productName, Integer minimum, Long quantity) {
        this.id = id;
        this.productName = productName;
        this.minimum = minimum;
        this.quantity = quantity;
    }
}
