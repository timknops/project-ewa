package nl.solar.app.repositories;

import nl.solar.app.models.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("PRODUCTS.INMEMORY")
public class ProductRepositoryMock implements EntityRepository<Product> {
    private List<Product> products;
    private final long STARTING_ID = 3000;
    private long currentId = STARTING_ID;
    private final String[] productNames = {
            "Enphase IQ8+ omvormer",
            "Enphase Q kabel 1 fase",
            "Gateway / envoy",
            "Enphase IQ8M  omvormer",
            "Enphase Q Relay 1 fase ",
            "Enphase Q Relay 3 fase ",
            "MB glas/glas 380"
    };

    public ProductRepositoryMock() {
        products = new ArrayList<>();
        for (String productName : productNames) {
            Product product = Product.createDummyProducts(currentId, productName, "Lorem ipsum dolor sit amet, consectetur adipisicing elit.");
            products.add(product);
            currentId +=3;
        }
    }

    @Override
    public List<Product> findALL() {
        return products;
    }

    @Override
    public Product findById(long id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Product delete(long id) {
        Product toBeDeleted = this.findById(id);

        if (toBeDeleted != null) {
            products.remove(toBeDeleted);
        }
        return toBeDeleted;
    }

    @Override
    public Product save(Product item) {
        int index = products.indexOf(item);
        if (index != -1) {
            products.set(index, item);
        } else {
            //update the id if no id was given by the user
            if (item.getId() == 0) {
                item.setId(currentId);
                currentId += 3;
            }
            products.add(item);
        }
        return item;
    }
}
