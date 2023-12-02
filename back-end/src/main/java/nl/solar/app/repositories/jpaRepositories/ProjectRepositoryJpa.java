package nl.solar.app.repositories.jpaRepositories;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.solar.app.repositories.ProjectRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Project;

/**
 * JPA implementation of the ProjectRepository.
 * 
 * @author Tim Knops
 */
@Repository("PROJECTS.JPA")
@Transactional
@Primary
public class ProjectRepositoryJpa implements ProjectRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Finds all projects.
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
     * @param id the id of the project to find.
     * @return The project with the given id.
     */
    @Override
    public Project findById(long id) {
        return entityManager.find(Project.class, id);
    }

    /**
     * Deletes a project.
     * 
     * @param id the id of the project to delete.
     * @return The deleted project.
     */
    @Override
    public Project delete(long id) {
        Project toBeDeleted = findById(id);

        if (toBeDeleted == null)
            return null;

        entityManager.remove(toBeDeleted);
        return toBeDeleted;
    }

    /**
     * Saves a project.
     * 
     * @param newProject the project to save.
     * @return The saved project.
     */
    @Override
    public Project save(Project newProject) {
        return entityManager.merge(newProject);
    }

    /**
     * This method executes a query and returns a list of maps containing the id and
     * name of the query result.
     * 
     * @param queryString the query string to execute
     * @param idField     the name of the id field in the query result
     * @param nameField   the name of the name field in the query result
     * @return A list of maps containing the id and name of the query result.
     */
    private List<Map<String, Object>> executeQuery(String queryString, String idField, String nameField) {
        List<Object[]> queryResult = entityManager.createQuery(queryString, Object[].class)
                .getResultList();

        // Format the query result to a list of maps.
        return queryResult.stream()
                .map(row -> Map.of("id", row[0], nameField, row[1])) // Map the query result to a map.
                .collect(Collectors.toList()); // Collect the mapped objects into a list.
    }

    /**
     * Retrieves a list of all teams.
     * 
     * @return A list of all teams.
     */
    @Override
    public List<Map<String, Object>> getTeamsInfo() {
        return executeQuery("SELECT t.id, t.team FROM Team t", "team", "team");
    }

    /**
     * Retrieves a list of all warehouses.
     * 
     * @return A list of all warehouses.
     */
    @Override
    public List<Map<String, Object>> getWarehousesInfo() {
        return executeQuery("SELECT w.id, w.name FROM Warehouse w", "name", "name");
    }

    /**
     * Retrieves a list of all products.
     * 
     * @return A list of all products.
     */
    @Override
    public List<Map<String, Object>> getProductsInfo() {
        return executeQuery("SELECT p.id, p.productName FROM Product p", "productName", "product_name");
    }

}