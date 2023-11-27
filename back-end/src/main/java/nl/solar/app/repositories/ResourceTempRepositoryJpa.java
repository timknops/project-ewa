package nl.solar.app.repositories;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.ResourceTemp;

/**
 * Repository implementation for managing ResourceTemp entities using JPA.
 * 
 * @see EntityRepository
 * @see ResourceTemp
 * 
 * @author Tim Knops
 */
@Repository("RESOURCES_TEMP.JPA") // TODO: Rename to RESOURCES.JPA when refactor is done.
@Transactional
@Primary
public class ResourceTempRepositoryJpa implements EntityRepository<ResourceTemp> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves all ResourceTemp entities from the database.
     *
     * @return A list of ResourceTemp entities.
     */
    @Override
    public List<ResourceTemp> findAll() {
        TypedQuery<ResourceTemp> query = entityManager.createQuery("SELECT r FROM ResourceTemp r", ResourceTemp.class);
        return query.getResultList();
    }

    /**
     * Retrieves a ResourceTemp entity by its ID.
     *
     * @param id The ID of the ResourceTemp entity.
     * @return The ResourceTemp entity with the specified ID.
     */
    @Override
    public ResourceTemp findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    /**
     * Deletes a ResourceTemp entity by its ID.
     *
     * @param id The ID of the ResourceTemp entity to delete.
     * @return The deleted ResourceTemp entity.
     */
    @Override
    public ResourceTemp delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * Saves a ResourceTemp entity to the database.
     *
     * @param item The ResourceTemp entity to save.
     * @return The saved ResourceTemp entity.
     */
    @Override
    public ResourceTemp save(ResourceTemp item) {
        return entityManager.merge(item);
    }
}
