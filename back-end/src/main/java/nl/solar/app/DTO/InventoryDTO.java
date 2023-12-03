package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.models.Warehouse;
import nl.solar.app.models.views.ResourceView;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing an inventory, including warehouse information and a list of products.
 * This DTO is used to transfer inventory-related data between the backend and frontend with specific view annotations.
 *
 * @author Julian Kruithof
 */
public class InventoryDTO {

    @JsonView(ResourceView.Complete.class)
    private Warehouse warehouse;
    @JsonView(ResourceView.Complete.class)
    private List<InventoryProductDTO> products;

    /**
     * Constructs an InventoryDTO with the specified warehouse and product DTOs.
     *
     * @param warehouse The warehouse associated with the inventory.
     * @param products  The list of product DTOs associated with the inventory.
     */
    public InventoryDTO(Warehouse warehouse, List<InventoryProductDTO> products) {
        this.warehouse = warehouse;
        this.products = products;
    }
}
