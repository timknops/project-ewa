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
 * Repository implementation for managing Resource entities using JPA.
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
     * Retrieves all Resource entities from the database.
     *
     * @return A list of Resource entities.
     */
    @Override
    public List<Resource> findAll() {
        TypedQuery<Resource> query = entityManager.createQuery("SELECT r FROM Resource r", Resource.class);
        return query.getResultList();
    }

    /**
     * Retrieves a Resource entity by its ID.
     *
     * @param id The ID of the Resource entity.
     * @return The Resource entity with the specified ID.
     */
    @Override
    public Resource findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    /**
     * Deletes a Resource entity by its ID.
     *
     * @param id The ID of the Resource entity to delete.
     * @return The deleted Resource entity.
     */
    @Override
    public Resource delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * Saves a Resource entity to the database.
     *
     * @param item The Resource entity to save.
     * @return The saved Resource entity.
     */
    @Override
    public Resource save(Resource item) {
        return entityManager.merge(item);
    }
}
