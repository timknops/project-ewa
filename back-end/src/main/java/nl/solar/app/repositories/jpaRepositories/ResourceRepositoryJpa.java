package nl.solar.app.repositories.jpaRepositories;

import java.util.List;
import java.util.Map;
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
import nl.solar.app.models.Resource;

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

    @Override
    public List<Resource> findAll() {
        TypedQuery<Resource> query = entityManager.createQuery("SELECT r FROM Resource r", Resource.class);
        return query.getResultList();
    }

    @Override
    public Resource findByIds(long firstId, long secondId) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIds'");
    }

    @Override
    public Resource deleteByIds(long firstId, long secondId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByIds'");
    }

    @Override
    public Resource save(Resource item) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    /**
     * Retrieves the project resources for a given project ID.
     * 
     * @param projectId the ID of the project
     * @return a list of ProjectResourceDTO objects representing the project
     *         resources
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
}
