package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.DTO.ItemDTO;
import nl.solar.app.models.Item;
import nl.solar.app.models.Order;
import nl.solar.app.models.compositeKeys.ItemKey;
import nl.solar.app.repositories.ItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * JPA implementation of the repository for managing {@link Item} entities.
 * This repository provides CRUD operations related to item using Java Persistence API (JPA).
 *
 * @author Julian Kruithof
 */
@Repository
@Transactional
public class ItemRepositoryJpa implements ItemRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Item> findAll() {
        return entityManager.createQuery("SELECT i FROM Item i", Item.class).getResultList();
    }

    @Override
    public Item findByIds(long productId, long orderId) {
        ItemKey itemKey = new ItemKey();
        itemKey.setProductId(productId);
        itemKey.setOrderId(orderId);
        return entityManager.find(Item.class, itemKey);
    }

    @Override
    public Item deleteByIds(long productId, long orderId) {
        Item toBeDeleted = findByIds(productId, orderId);
        if(toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }
        return toBeDeleted;
    }

    @Override
    public List<ItemDTO> getItemsForOrder(long orderId) {
        return entityManager.createQuery("SELECT NEW nl.solar.app.DTO.ItemDTO(i.product, i.quantity) FROM Item i WHERE i.compositeId.orderId = :order_id", ItemDTO.class)
                .setParameter("order_id", orderId)
                .getResultList();
    }

    @Override
    public Item save(Item item) {
        return entityManager.merge(item);
    }
}
