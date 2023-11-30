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
public class ResourceKey implements Serializable {

    @Column(name = "project_id")
    private long projectId;

    @Column(name = "product_id")
    private long productId;

    public ResourceKey(long projectId, long productId) {
        this.projectId = projectId;
        this.productId = productId;
    }

    public ResourceKey() {
    }

    public long getProjectId() {
        return projectId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public void setProductId(long productId) {
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
