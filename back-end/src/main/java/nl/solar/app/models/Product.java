package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import nl.solar.app.models.views.ResourceView;

import java.util.*;

/**
 * Class which represents a product sold by Solar Sedum
 *
 * @author Julian Kruithof
 */
@Entity
public class Product {

    @Id
    @JsonView(ResourceView.Complete.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonView(ResourceView.Complete.class)
    private String productName;

    @JsonView(ResourceView.Complete.class)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Resource> resources;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Inventory> inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Item> items;


    public Product() {
        resources = new HashSet<>();
        inventory = new HashSet<>();
        items = new HashSet<>();
    }

    /**
     * create an dummy product by using the default constructor and the getters and
     * setters
     *
     * @param id          - the id of the product
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

    public Set<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(Set<Inventory> inventory) {
        this.inventory = inventory;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
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

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

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
