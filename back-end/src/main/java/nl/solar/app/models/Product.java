package nl.solar.app.models;

import java.util.Objects;

/**
 * Class which represents a product sold by Solar Sedum
 *
 * @author Julian Kruithof
 */
public class Product {
    private long id;
    private String productName;
    private String description;

    /**
     * create an dummy product by using the default constructor and the getters and setters
     * @param id - the id of the product
     * @param productName the name of the product
     * @param description the description of the product
     * @return a Dummy product
     */
    public static Product createDummyProducts(long id, String productName, String description) {
        Product dummyProduct = new Product();
        dummyProduct.setId(id);
        dummyProduct.setProductName(productName);
        dummyProduct.setDescription(description);
        return dummyProduct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof Product product) {
            return this.getId() == product.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
