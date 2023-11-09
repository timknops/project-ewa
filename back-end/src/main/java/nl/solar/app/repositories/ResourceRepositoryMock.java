package nl.solar.app.repositories;

import nl.solar.app.models.Product;
import nl.solar.app.models.Resource;
import nl.solar.app.models.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("RESOURCES.INMEMORY")
public class ResourceRepositoryMock implements ResourceRepository {
    private List<Resource> resources;

    //To be changed to the corresponding repository
    private final List<Warehouse> warehouses = List.of(
            new Warehouse(3000, "Solar Sedum", ""),
            new Warehouse(3003, "Superzon", ""),
            new Warehouse(3006, "EHES", ""),
            new Warehouse(3009, "The Switch", ""));

    private final EntityRepository<Product> productRepo;

    @Autowired
    public ResourceRepositoryMock(EntityRepository<Product> productRepo) {
        this.productRepo = productRepo;
        this.resources = new ArrayList<>();
        List<Product> products = this.productRepo.findALL();

        for (Warehouse warehouse: warehouses) {
            for (Product product: products) {
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
                .filter(resource -> resource.getWarehouse().getId() == warehouseId && resource.getProduct().getId() == productId)
                .findFirst().orElse(null);
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
