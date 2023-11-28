package nl.solar.app.repositories.jpaRepositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.solar.app.repositories.EntityRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import nl.solar.app.models.Project;

/**
 * JPA implementation of the ProjectRepository.
 * 
 * @author Tim Knops
 */
@Repository("PROJECTS.JPA")
@Primary
public class ProjectRepositoryJpa implements EntityRepository<Project> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns a list of all projects.
     * 
     * @return List<Project> A list of all projects.
     */
    @Override
    public List<Project> findAll() {
        TypedQuery<Project> query = entityManager.createQuery("SELECT p FROM Project p", Project.class);
        return query.getResultList();
    }

    /**
     * Finds a project by id.
     * 
     * @param id The id of the project to find.
     * @return Project The project with the given id.
     */
    @Override
    public Project findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    /**
     * Deletes a project.
     * 
     * @param id The id of the project to delete.
     * @return Project The deleted project.
     */
    @Override
    public Project delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * Saves a project.
     * 
     * @param newProject The project to save.
     * @return Project The saved project.
     */
    @Override
    public Project save(Project newProject) {
        return entityManager.merge(newProject);
    }

    /**
     * Retrieves the information needed to display the add modal.
     * 
     * @return Map<String, List<String>> A map containing the information needed to
     *         display the add modal.
     */
    public Map<String, List<String>> getAddModalInfo() {
        // Get all team names.
        TypedQuery<String> teamQuery = entityManager.createQuery("SELECT t.team FROM Team t", String.class);
        List<String> teams = teamQuery.getResultList();

        // Get all product names.
        TypedQuery<String> productQuery = entityManager.createQuery("SELECT p.productName FROM Product p",
                String.class);
        List<String> productNames = productQuery.getResultList();

        // TODO: Get all warehouse names using a query via the prodcuts. Can be done
        // when the warehouse is implemented.

        // Create a map to store the information.
        Map<String, List<String>> addModalInfo = new HashMap<>();
        addModalInfo.put("teams", teams);
        addModalInfo.put("productNames", productNames);

        return addModalInfo;
    }
}