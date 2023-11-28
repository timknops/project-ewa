package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("WAREHOUSE.JPA")
@Primary
@Transactional
public class WarehouseRepositoryJpa implements EntityRepository<Warehouse> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Warehouse> findAll() {
        return entityManager.createQuery("SELECT w from Warehouse w", Warehouse.class).getResultList();
    }

    @Override
    public Warehouse findById(long id) {
        return entityManager.find(Warehouse.class, id);
    }

    @Override
    public Warehouse delete(long id) {
        Warehouse toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }
        return toBeDeleted;
    }

    @Override
    public Warehouse save(Warehouse item) {
        return entityManager.merge(item);
    }
}
