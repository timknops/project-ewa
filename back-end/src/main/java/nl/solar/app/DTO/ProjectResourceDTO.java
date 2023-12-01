package nl.solar.app.DTO;

import nl.solar.app.models.Product;

public class ProjectResourceDTO {

  private Product product;
  private int quantity;

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
