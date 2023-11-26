package nl.solar.app.repositories;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.ResourceTemp;

@Repository("RESOURCES_TEMP.JPA") // TODO: Rename to RESOURCES.JPA when refactor is done.
@Transactional
@Primary
public class ResourceTempRepositoryJpa implements EntityRepository<ResourceTemp> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ResourceTemp> findAll() {
        TypedQuery<ResourceTemp> query = entityManager.createQuery("SELECT r FROM ResourceTemp r", ResourceTemp.class);
        return query.getResultList();
    }

    @Override
    public ResourceTemp findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public ResourceTemp delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public ResourceTemp save(ResourceTemp item) {
        return entityManager.merge(item);
    }
}
