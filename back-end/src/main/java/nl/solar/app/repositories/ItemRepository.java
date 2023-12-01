package nl.solar.app.repositories;

import nl.solar.app.models.Item;
import nl.solar.app.repositories.ManyToManyRepository;

/**
 * interface for a itemRepository for extra functionality outside the existing many-to-many functions
 *
 * @author Julian Kruithof
 */
public interface ItemRepository extends ManyToManyRepository<Item> {
}
