package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Product;
import nl.solar.app.models.Warehouse;
import nl.solar.app.models.compositeKeys.InventoryKey;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("INVENTORY.JPA")
@Primary
@Transactional
public class InventoryRepositoryJpa implements InventoryRepository {

    private final EntityRepository<Product> productRepo;
    private final EntityRepository<Warehouse> warehouseRepo;

    public InventoryRepositoryJpa(EntityRepository<Product> productRepo, EntityRepository<Warehouse> warehouseRepo) {
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Inventory> findAll() {
        return entityManager.createQuery("SELECT i FROM Inventory i", Inventory.class).getResultList();
    }

    @Override
    public Inventory findByIds(long productId, long warehouseId) {
        InventoryKey key = new InventoryKey();
        key.setProductId(productId);
        key.setWarehouseId(warehouseId);

        return entityManager.find(Inventory.class, key);
    }

    @Override
    public List<Inventory> findInventoryForWarehouse(long warehouseId) {
        return entityManager
                .createQuery("SELECT i FROM Inventory i WHERE i.id.warehouseId = :warehouse_id", Inventory.class)
                .setParameter("warehouse_id", warehouseId)
                .getResultList();
    }

    @Override
    public List<Product> findProductsWithoutInventory(long warehouseId) {
        return entityManager.createQuery("SELECT p FROM Product p " +
                " WHERE p.id NOT IN (SELECT i.id.productId FROM Inventory i WHERE i.id.warehouseId = :warehouse_id)",
                Product.class)
                .setParameter("warehouse_id", warehouseId)
                .getResultList();
    }

    @Override
    public Inventory deleteByIds(long productId, long warehouseId) {
        Inventory toBeDeleted = findByIds(productId, warehouseId);

        if (toBeDeleted != null) {
            entityManager.remove(toBeDeleted);
        }

        return toBeDeleted;
    }

    @Override
    public Inventory save(Inventory item) {
        return entityManager.merge(item);
    }

}
