package nl.solar.app.repositories.jpaRepositories;

import java.util.List;
import java.util.stream.Collectors;

import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.ResourceRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.DTO.ProjectResourceDTO;
import nl.solar.app.models.Product;
import nl.solar.app.models.Project;
import nl.solar.app.models.Resource;
import nl.solar.app.models.compositeKeys.ResourceKey;

/**
 * Repository implementation for managing Resource entities using JPA.
 * 
 * @see EntityRepository
 * @see Resource
 * 
 * @author Tim Knops
 */
@Repository("RESOURCES.JPA")
@Transactional
@Primary
public class ResourceRepositoryJpa implements ResourceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Finds all resources.
     * 
     * @return List<Resource> A list of all resources.
     */
    @Override
    public List<Resource> findAll() {
        TypedQuery<Resource> query = entityManager.createQuery("SELECT r FROM Resource r", Resource.class);
        return query.getResultList();
    }

    /**
     * Finds a resource by id.
     * 
     * @param firstId  The first id of the resource to find.
     * @param secondId The second id of the resource to find.
     * @return Resource The resource with the given id.
     */
    @Override
    public Resource findByIds(long firstId, long secondId) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIds'");
    }

    /**
     * Deletes a resource.
     * 
     * @param firstId  The first id of the resource to delete.
     * @param secondId The second id of the resource to delete.
     * @return Resource The deleted resource.
     */
    @Override
    public Resource deleteByIds(long firstId, long secondId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByIds'");
    }

    /**
     * Saves a resource.
     * 
     * @param item The resource to save.
     * @return Resource The saved resource.
     */
    @Override
    public Resource save(Resource item) {
        return entityManager.merge(item);
    }

    /**
     * Retrieves the project resources for a given project ID.
     * 
     * @param projectId the ID of the project
     * @return A list of ProjectResourceDTO objects representing the project
     *         resources,
     */
    @Override
    public List<ProjectResourceDTO> getProjectResources(long projectId) {
        // Execute a query to retrieve the product and quantity of resources for a given
        // project ID.
        List<Object[]> results = entityManager
                .createQuery("SELECT r.product, r.quantity FROM Resource r WHERE r.project.id = :projectId",
                        Object[].class)
                .setParameter("projectId", projectId).getResultList();

        // Map the query results to ProjectResourceDTO objects.
        return results.stream().map(r -> {
            ProjectResourceDTO projectResourceDTO = new ProjectResourceDTO();
            projectResourceDTO.setProduct((Product) r[0]); // Set the product from the query result.
            projectResourceDTO.setQuantity((Integer) r[1]); // Set the quantity from the query result.
            return projectResourceDTO;
        }).collect(Collectors.toList()); // Collect the mapped objects into a list.
    }

    /**
     * Adds resources to a project.
     * 
     * @param projectId the ID of the project
     * @param resources A list of ProjectResourceDTO objects representing the
     *                  resources to add.
     */
    @Override
    public void addProjectResources(long projectId, List<ProjectResourceDTO> resources) {
        // Get the project from the database.
        Project project = entityManager.find(Project.class, projectId);

        // Loop through all resources.
        for (ProjectResourceDTO resource : resources) {
            // Load the Product entity within the current persistence context.
            Product product = entityManager.find(Product.class, resource.getProduct().getId());

            // Create a new Resource object.
            Resource newResource = new Resource();
            ResourceKey key = new ResourceKey();
            key.setProjectId(projectId);
            key.setProductId(resource.getProduct().getId());

            newResource.setId(key); // Set the ID for the resource.
            newResource.setProject(project); // Set the project for the resource.
            newResource.setProduct(product); // Set the product for the resource.
            newResource.setQuantity(resource.getQuantity()); // Set the quantity for the resource.

            entityManager.persist(newResource); // Persist the resource to the database.
        }
    }

    /**
     * Deletes all resources for a given project ID.
     * 
     * @param projectId the ID of the project
     */
    @Override
    public void deleteProjectResources(long projectId) {
        // Execute a query to delete all resources for a given project ID.
        entityManager.createQuery("DELETE FROM Resource r WHERE r.project.id = :projectId")
                .setParameter("projectId", projectId).executeUpdate();
    }

}
