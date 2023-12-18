package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.DTO.InventoryProductDTO;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Product;
import nl.solar.app.models.compositeKeys.InventoryKey;
import nl.solar.app.repositories.InventoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the repository for managing {@link Inventory} entities.
 * This repository provides CRUD operations related to inventory using Java
 * Persistence API (JPA).
 *
 * @author Julian Kruithof
 */
@Repository("INVENTORY.JPA")
@Primary
@Transactional
public class InventoryRepositoryJpa implements InventoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public InventoryRepositoryJpa() {
    }

    /**
     * Retrieves all inventories from the database.
     *
     * @return a list of all inventories
     */
    @Override
    public List<Inventory> findAll() {
        return entityManager.createQuery("SELECT i FROM Inventory i", Inventory.class).getResultList();
    }

    /**
     * Retrieves an inventory by its product and warehouse IDs from the database.
     *
     * @param productId   the ID of the product
     * @param warehouseId the ID of the warehouse
     * @return the inventory with the specified product and warehouse IDs, or
     *         {@code null} if not found
     */
    @Override
    public Inventory findByIds(long productId, long warehouseId) {
        InventoryKey key = new InventoryKey();
        key.setProductId(productId);
        key.setWarehouseId(warehouseId);

        return entityManager.find(Inventory.class, key);
    }

    /**
     * Retrieves all inventories for a specific warehouse from the database.
     *
     * @param warehouseId the ID of the warehouse
     * @return a list of inventories for the specified warehouse
     */
    @Override
    public List<InventoryProductDTO> findInventoryForWarehouse(long warehouseId) {
        return entityManager
                .createQuery("SELECT new nl.solar.app.DTO.InventoryProductDTO(i.product.id, i.product.productName, i.minimum, i.quantity)" +
                        " FROM Inventory i WHERE i.id.warehouseId = :warehouse_id", InventoryProductDTO.class)
                .setParameter("warehouse_id", warehouseId)
                .getResultList();
    }

    /**
     * Retrieves products without inventory for a specific warehouse from the
     * database.
     *
     * @param warehouseId the ID of the warehouse
     * @return a list of products without inventory for the specified warehouse
     */
    @Override
    public List<Product> findProductsWithoutInventory(long warehouseId) {
        return entityManager.createQuery("SELECT p FROM Product p " +
                " WHERE p.id NOT IN (SELECT i.id.productId FROM Inventory i WHERE i.id.warehouseId = :warehouse_id)",
                Product.class)
                .setParameter("warehouse_id", warehouseId)
                .getResultList();
    }

    /**
     * Deletes an inventory by its product and warehouse IDs from the database.
     *
     * @param productId   the ID of the product
     * @param warehouseId the ID of the warehouse
     * @return the deleted inventory, or {@code null} if the inventory with the
     *         specified IDs does not exist
     */
    @Override
    public Inventory deleteByIds(long productId, long warehouseId) {
        Inventory toBeDeleted = findByIds(productId, warehouseId);

        if (toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }

        return toBeDeleted;
    }

    /**
     * Saves or updates an inventory item in the database.
     *
     * @param item the inventory item to save or update
     * @return the saved or updated inventory item
     */
    @Override
    public Inventory save(Inventory item) {
        return entityManager.merge(item);
    }
}
