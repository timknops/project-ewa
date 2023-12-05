package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Order;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository implementation for a order entity using jpa
 *
 * @author Julian Kruithof
 */
@Repository
@Transactional
public class OrderRepositoryJpa implements EntityRepository<Order> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Order> findAll() {
        return entityManager.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    @Override
    public Order findById(long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public Order delete(long id) {
        Order toBeDeleted = findById(id);

        if (toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }

        return toBeDeleted;
    }

    @Override
    public Order save(Order item) {
        return entityManager.merge(item);
    }
}
