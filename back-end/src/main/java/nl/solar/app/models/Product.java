package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import nl.solar.app.models.views.ResourceView;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<ResourceTemp> projects = new HashSet<>();

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

    public Set<ResourceTemp> getProjects() {
        return projects;
    }

    public void setProjects(Set<ResourceTemp> projects) {
        this.projects = projects;
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
