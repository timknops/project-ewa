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

    /**
     * Find all warehouses
     * @return a list of warehouses
     */
    @Override
    public List<Warehouse> findAll() {
        return entityManager.createQuery("SELECT w from Warehouse w", Warehouse.class).getResultList();
    }

    /**
     * Find a warehouse for a specific id
     * @param id the ID of the entity to retrieve
     * @return A warehouse
     */
    @Override
    public Warehouse findById(long id) {
        return entityManager.find(Warehouse.class, id);
    }

    /**
     * Delete a warehouse
     * @param id the ID of the entity to delete
     * @return The warehouse to be deleted
     */
    @Override
    public Warehouse delete(long id) {
        Warehouse toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }
        return toBeDeleted;
    }

    /**
     * Add or update an order
     * @param item the entity to save
     * @return The saved warehouse
     */
    @Override
    public Warehouse save(Warehouse item) {
        return entityManager.merge(item);
    }
}
