package nl.solar.app.repositories;

import nl.solar.app.models.Order;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;

import java.util.List;


/**
 * Repository for the {@link Order} entity, this repository is for extra functionality which isn't covered by the many to many
 *
 * @author Julian Kruithof
 */
public interface OrderRepository extends EntityRepository<Order> {
    List<Order> findOrdersWarehouse(long wId);

    List<Order> findPendingOrders(Warehouse warehouse);

    List<Order> findAllPendingOrders();
}
