package nl.solar.app.repositories;

import nl.solar.app.DTO.ItemDTO;
import nl.solar.app.models.Item;

import java.util.List;


/**
 * interface for a itemRepository for extra functionality outside the existing many-to-many functions
 *
 * @author Julian Kruithof
 */
public interface ItemRepository extends ManyToManyRepository<Item> {

    List<Item> getItemsForOrder(long orderId) ;
}
