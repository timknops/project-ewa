package nl.solar.app.repositories.jpaRepositories;

import java.util.List;

import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.ResourceRepository;

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
public class ResourceRepositoryJpa implements ResourceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Resource> findAll() {
        TypedQuery<Resource> query = entityManager.createQuery("SELECT r FROM Resource r", Resource.class);
        return query.getResultList();
    }

    @Override
    public Resource findByIds(long firstId, long secondId) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIds'");
    }

    @Override
    public Resource deleteByIds(long firstId, long secondId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByIds'");
    }

    @Override
    public Resource save(Resource item) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
