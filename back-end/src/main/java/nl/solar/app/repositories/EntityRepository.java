package nl.solar.app.repositories;

import java.util.List;

/**
 * This interface represents a generic repository for managing entities.
 *
 * @param <E> the type of entity managed by the repository
 */
public interface EntityRepository<E> {

    /**
     * Retrieves all entities from the repository.
     *
     * @return a list of all entities
     */
    List<E> findAll();

    /**
     * Retrieves an entity by its ID from the repository.
     *
     * @param id the ID of the entity to retrieve
     * @return the entity with the specified ID, or null if not found
     */
    E findById(long id);

    /**
     * Deletes an entity by its ID from the repository.
     *
     * @param id the ID of the entity to delete
     * @return the deleted entity, or null if not found
     */
    E delete(long id);

    /**
     * Saves an entity to the repository.
     *
     * @param item the entity to save
     * @return the saved entity
     */
    E save(E item);
}
