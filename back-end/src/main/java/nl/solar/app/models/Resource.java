package nl.solar.app.models;

import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import nl.solar.app.models.compositeKeys.ResourceKey;

/**
 * Represents a resource.
 * 
 * @author Tim Knops
 */
@Entity
public class Resource {

  @EmbeddedId
  private ResourceKey id;

  @ManyToOne
  @MapsId("projectId")
  @JoinColumn(name = "project_id")
  // @JsonManagedReference
  private Project project;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  // @JsonManagedReference("product_resource")
  private Product product;

  private int quantity;

  /**
   * Creates a resource with a project, product and quantity.
   * 
   * @param project  - the project
   * @param product  - the product
   * @param quantity - the quantity
   */
  public Resource(Project project, Product product, int quantity) {
    this.project = project;
    this.product = product;
    this.quantity = quantity;
    this.id = new ResourceKey(project.getId(), product.getId());
  }

  public Resource() {
  }

  public ResourceKey getId() {
    return id;
  }

  public void setId(ResourceKey id) {
    this.id = id;
  }

  public Project getProject() {
    return project;
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Resource))
      return false;

    Resource that = (Resource) o;
    return Objects.equals(getProject(), that.getProject()) && Objects.equals(getProduct(), that.getProduct());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProject().getId(), getProduct().getId());
  }

}
