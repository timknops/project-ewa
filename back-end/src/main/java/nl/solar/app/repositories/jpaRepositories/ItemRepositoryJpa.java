package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Item;
import nl.solar.app.models.compositeKeys.ItemKey;
import nl.solar.app.repositories.ManyToManyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the repository for managing {@link Item} entities.
 * This repository provides CRUD operations related to item using Java Persistence API (JPA).
 *
 * @author Julian Kruithof
 */
@Repository
@Transactional
public class ItemRepositoryJpa implements ManyToManyRepository<Item> {

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
    public Item save(Item item) {
        return entityManager.merge(item);
    }
}
