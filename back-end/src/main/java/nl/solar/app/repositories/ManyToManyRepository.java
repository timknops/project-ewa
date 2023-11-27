package nl.solar.app.repositories;

import java.util.List;

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
     * @param firstId the id of the first item which is used to combine the many to many relation
     * @param secondId the id of the second item which is used to combine the many to many relation
     * @return a item object
     */
    E findByIds(long firstId, long secondId);

    /**
     * Delete a specific item
     * @param firstId the id of the first item which is used to combine the many to many relation
     * @param secondId the id of the second item which is used to combine the many to many relation
     * @return the deleted item
     */
    E deleteByIds(long firstId, long secondId);

    /**
     * update or add an item
     * @param item the item to be added or deleted
     * @return the added or deleted item.
     */
    E save(E item);
}
