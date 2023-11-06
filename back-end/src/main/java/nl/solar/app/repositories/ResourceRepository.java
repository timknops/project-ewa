package nl.solar.app.repositories;

import nl.solar.app.models.Resource;

import java.util.List;

/**
 * Since a Resource is a lookup between a product and a warehouse it isn't possible to use the global entityRepo
 * Therefore is has its own.
 *
 * @author Julian Kruithof
 */
public interface ResourceRepository {
    List<Resource> findAll();

    Resource findResource(long warehouseId, long productId);

    List<Resource> findResourceForWarehouse(long warehouseId);

    Resource deleteResource(long warehouseId, long productId);

    Resource saveResource(Resource resource);
}
