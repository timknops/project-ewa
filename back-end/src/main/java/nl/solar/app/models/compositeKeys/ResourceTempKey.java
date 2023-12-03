package nl.solar.app.models.compositeKeys;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents a composite key for the ResourceTemp class.
 * 
 * @author Tim Knops
 */
@Embeddable
public class ResourceTempKey implements Serializable {

  @Column(name = "project_id")
  Long projectId;

  @Column(name = "product_id")
  Long productId;

  public ResourceTempKey() {
  }

  public ResourceTempKey(Long projectId, Long productId) {
    this.projectId = projectId;
    this.productId = productId;
  }

  public Long getProjectId() {
    return projectId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof ResourceTempKey))
      return false;

    ResourceTempKey that = (ResourceTempKey) o;
    return getProjectId().equals(that.getProjectId()) && getProductId().equals(that.getProductId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProjectId(), getProductId());
  }

}
