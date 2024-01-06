package nl.solar.app.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.solar.app.models.Product;

/**
 * A dto to represent an item, for which it is not know which order id it shall get, this is handled in the controller
 *
 * @author Julian Kruithof
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ItemDTO {
    private Product product;
    private long quantity;
}
