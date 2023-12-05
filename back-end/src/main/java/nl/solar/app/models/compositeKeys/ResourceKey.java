package nl.solar.app.models.compositeKeys;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents a composite key for the Resource class.
 * 
 * @author Tim Knops
 */
@Embeddable
public class ResourceKey implements Serializable {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "product_id")
    private Long productId;

    public ResourceKey(Long projectId, Long productId) {
        this.projectId = projectId;
        this.productId = productId;
    }

    public ResourceKey() {
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
        if (!(o instanceof ResourceKey))
            return false;

        ResourceKey that = (ResourceKey) o;
        return getProjectId() == that.getProjectId() && getProductId() == that.getProductId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProjectId(), getProductId());
    }
}
