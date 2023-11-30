package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.models.Warehouse;
import nl.solar.app.models.views.ResourceView;

import java.util.List;


public class InventoryDTO {

    @JsonView(ResourceView.Complete.class)
    private Warehouse warehouse;
    @JsonView(ResourceView.Complete.class)
    private List<ProductDTO> products;

    public InventoryDTO(Warehouse warehouse, List<ProductDTO> products) {
        this.warehouse = warehouse;
        this.products = products;
    }
}
