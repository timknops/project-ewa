package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.enums.OrderStatus;
import nl.solar.app.models.Order;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository implementation for a order entity using jpa
 *
 * @author Julian Kruithof
 */
@Repository
@Transactional
public class OrderRepositoryJpa implements OrderRepository {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Find all orders
     * @return a list of orders
     */
    @Override
    public List<Order> findAll() {
        return entityManager.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    /**
     * Find one specific order
     * @param id the ID of the entity to retrieve
     * @return a order object
     */
    @Override
    public Order findById(long id) {
        return entityManager.find(Order.class, id);
    }

    /**
     * Find all orders for a specific warehouse
     * @param wId the id of a warehouse
     * @return a list of orders
     */
    @Override
    public List<Order> findOrdersWarehouse(long wId) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.warehouse.id = ?1", Order.class)
                .setParameter(1, wId).getResultList();
    }


    /**
     * Find all pending orders for a specific warehouse
     * @param warehouse the warehouse for which the orders should be found
     * @return a list of orders
     */
    @Override
    public List<Order> findPendingOrders(Warehouse warehouse) {
        return entityManager.createQuery("SELECT o FROM Order o WHERE o.status = ?1 AND o.warehouse.id = ?2", Order.class)
                .setParameter(1, OrderStatus.PENDING)
                .setParameter(2, warehouse.getId())
                .getResultList();
    }

    /**
     * delete an order
     * @param id the ID of the entity to delete
     * @return the deleted order
     */
    @Override
    public Order delete(long id) {
        Order toBeDeleted = findById(id);

        if (toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }

        return toBeDeleted;
    }

    /**
     * Add or update an order
     * @param item the entity to save
     * @return the saved order
     */
    @Override
    public Order save(Order item) {
        return entityManager.merge(item);
    }
}
