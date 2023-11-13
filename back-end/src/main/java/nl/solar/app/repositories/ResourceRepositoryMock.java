package nl.solar.app.repositories;

import nl.solar.app.models.Product;
import nl.solar.app.models.Resource;
import nl.solar.app.models.Warehouse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("RESOURCES.INMEMORY")
public class ResourceRepositoryMock implements ResourceRepository {
    private List<Resource> resources;

    private final EntityRepository<Product> productRepo;
    private final EntityRepository<Warehouse> warehouseRepo;

    public ResourceRepositoryMock(EntityRepository<Product> productRepo, EntityRepository<Warehouse> warehouseRepo) {
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
        this.resources = new ArrayList<>();
        List<Product> products = this.productRepo.findAll();
        List<Warehouse> warehouses = this.warehouseRepo.findAll();

        for (Warehouse warehouse : warehouses) {
            for (Product product : products) {
                Resource resource = Resource.createDummyResource(warehouse, product);
                resources.add(resource);
            }
        }

    }

    @Override
    public List<Resource> findAll() {
        return resources;
    }

    @Override
    public List<Resource> findResourceForWarehouse(long warehouseId) {
        return resources.stream().filter(resource -> resource.getWarehouse().getId() == warehouseId).toList();
    }

    @Override
    public Resource findResource(long warehouseId, long productId) {
        return resources.stream()
                .filter(resource -> resource.getWarehouse().getId() == warehouseId
                        && resource.getProduct().getId() == productId)
                .findFirst().orElse(null);
    }

    @Override
    public void addResourcesForProduct(Product product) {
        for (Warehouse warehouse : warehouseRepo.findAll()) {
            Resource resource = Resource.createDummyResource(warehouse, product);
            resource.setQuantity(0);
            resources.add(resource);
        }
    }

    @Override
    public void addResourcesForWarehouse(Warehouse warehouse) {
        for (Product product : productRepo.findAll()) {
            Resource resource = Resource.createDummyResource(warehouse, product);
            resource.setQuantity(0);
            resources.add(resource);
        }
    }

    @Override
    public Resource deleteResource(long warehouseId, long productId) {
        Resource resource = findResource(warehouseId, productId);

        if (resource != null) {
            resources.remove(resource);
        }
        return resource;
    }

    @Override
    public void deleteResourcesForProduct(Product product) {
        for (Warehouse warehouse : warehouseRepo.findAll()) {
            deleteResource(warehouse.getId(), product.getId());
        }
    }

    @Override
    public void deleteResourcesForWarehouse(Warehouse warehouse) {
        for (Product product : productRepo.findAll()) {
            deleteResource(warehouse.getId(), product.getId());
        }
    }

    @Override
    public Resource saveResource(Resource resource) {
        int index = resources.indexOf(resource);
        System.out.println(index);
        if (index != -1) {
            resources.set(index, resource);
        } else {
            resources.add(resource);
        }
        return resource;
    }
}
