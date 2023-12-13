package nl.solar.app.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.solar.app.models.Warehouse;

import java.time.LocalDate;
import java.util.List;


/**
 * Since when adding order the id is not know, the items should not contain a reference to an order,
 * otherwise jpa creates empty orders. Therefore, a DTO is needed for adding items for a new order
 *
 * @author Julian Kruithof
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class OrderRequestDTO {
    private Warehouse warehouse;

    private String tag;

    private LocalDate deliverDate;

    private List<ItemDTO> items;

}
