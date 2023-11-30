package nl.solar.app.repositories.jpaRepositories;

import java.util.List;

import nl.solar.app.repositories.EntityRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Resource;

/**
 * Repository implementation for managing ResourceTemp entities using JPA.
 * 
 * @see EntityRepository
 * @see Resource
 * 
 * @author Tim Knops
 */
@Repository("RESOURCES.JPA")
@Transactional
@Primary
public class ResourceRepositoryJpa implements EntityRepository<Resource> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves all ResourceTemp entities from the database.
     *
     * @return A list of ResourceTemp entities.
     */
    @Override
    public List<Resource> findAll() {
        TypedQuery<Resource> query = entityManager.createQuery("SELECT r FROM ResourceTemp r", Resource.class);
        return query.getResultList();
    }

    /**
     * Retrieves a ResourceTemp entity by its ID.
     *
     * @param id The ID of the ResourceTemp entity.
     * @return The ResourceTemp entity with the specified ID.
     */
    @Override
    public Resource findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    /**
     * Deletes a ResourceTemp entity by its ID.
     *
     * @param id The ID of the ResourceTemp entity to delete.
     * @return The deleted ResourceTemp entity.
     */
    @Override
    public Resource delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * Saves a ResourceTemp entity to the database.
     *
     * @param item The ResourceTemp entity to save.
     * @return The saved ResourceTemp entity.
     */
    @Override
    public Resource save(Resource item) {
        return entityManager.merge(item);
    }
}
