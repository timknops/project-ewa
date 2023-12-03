package nl.solar.app.repositories;

import nl.solar.app.models.Inventory;
import nl.solar.app.models.Resource;

import java.util.List;

/**
 * A repository to handle all entities which have a many-to-many relation
 * for example {@link Inventory}
 * 
 * @param <E> the item which is a many-to-many link-table entity
 */
public interface ManyToManyRepository<E> {
    /**
     * Find all items and return it
     *
     *
     * @return a list of resources
     */
    List<E> findAll();

    /**
     * Find a specific item
     * 
     * @param firstId  the id of the first item which is used to combine the many to
     *                 many relation
     * @param secondId the id of the second item which is used to combine the many
     *                 to many relation
     * @return a item object
     */
    E findByIds(long firstId, long secondId);

    /**
     * Delete a specific item
     * 
     * @param firstId  the id of the first item which is used to combine the many to
     *                 many relation
     * @param secondId the id of the second item which is used to combine the many
     *                 to many relation
     * @return the deleted item
     */
    E deleteByIds(long firstId, long secondId);

    /**
     * update or add an item
     * 
     * @param item the item to be added or deleted
     * @return the added or deleted item.
     */
    Resource save(E item);
}
