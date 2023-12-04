package nl.solar.app.DTO;

import nl.solar.app.models.Product;

/**
 * Represents a DTO (Data Transfer Object) for a project resource.
 * 
 * @author Tim Knops
 */
public class ProjectResourceDTO {

  private Product product;
  private int quantity;

  /**
   * Constructs a new ProjectResourceDTO object with the specified product and
   * quantity.
   * 
   * @param product  the product associated with the project resource
   * @param quantity the quantity of the project resource
   */
  public ProjectResourceDTO(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public ProjectResourceDTO() {
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
