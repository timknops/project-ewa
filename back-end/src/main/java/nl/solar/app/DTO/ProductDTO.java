package nl.solar.app.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.models.views.ResourceView;


public class ProductDTO {

    @JsonView(ResourceView.Complete.class)
    private Long id;

    @JsonView(ResourceView.Complete.class)
    private String productName;

    @JsonView(ResourceView.Complete.class)
    private String description;

    @JsonView(ResourceView.Complete.class)
    private Long quantity;

    public ProductDTO(Long id, String productName, String description, Long quantity) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
    }
}
