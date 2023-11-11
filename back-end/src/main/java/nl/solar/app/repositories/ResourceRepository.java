package nl.solar.app.repositories;

import nl.solar.app.models.Product;
import nl.solar.app.models.Resource;
import nl.solar.app.models.Warehouse;

import java.util.List;

/**
 * Since a Resource is a lookup between a product and a warehouse it isn't possible to use the global entityRepo
 * Therefore is has its own.
 *
 * @author Julian Kruithof
 */
public interface ResourceRepository {

    /**
     * Find all resources and return it
     *
     *
     * @return a list of resources
     */
    List<Resource> findAll();

    /**
     * Find a specific resource
     * @param warehouseId the id of the warehouse
     * @param productId the id of the product
     * @return a resource object
     */
    Resource findResource(long warehouseId, long productId);

    /**
     * Find all resources for a specific warehouse
     * @param warehouseId the id of the warehouse
     * @return a list of resources where the warehouse is the same
     */
    List<Resource> findResourceForWarehouse(long warehouseId);

    /**
     * Add resources for a new product for all warehouses
     * @param product the newly created product
     */
    void addResourcesForProduct(Product product);

    /**
     * Add resources for all existing product for a new warehouse
     * @param warehouse the newly created warehouse
     */
    void addResourcesForWarehouse(Warehouse warehouse);

    /**
     * Delete a specific resource
     * @param warehouseId the id of the warehouse
     * @param productId the id of the product
     * @return the deleted resource
     */
    Resource deleteResource(long warehouseId, long productId);

    /**
     * delete resources connected to a product which is deleted
     * @param product the product recently deleted
     */
    void deleteResourcesForProduct(Product product);

    /**
     * delete resources connected to a warehouse which is deleted
     * @param warehouse the warehouse recently deleted
     */
    void deleteResourcesForWarehouse(Warehouse warehouse);

    /**
     * update or add a resource
     * @param resource the resource to be added or deleted
     * @return the added or deleted resource.
     */
    Resource saveResource(Resource resource);
}
