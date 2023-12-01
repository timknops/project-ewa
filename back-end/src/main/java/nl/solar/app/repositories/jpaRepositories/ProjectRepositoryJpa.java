package nl.solar.app.repositories.jpaRepositories;

import java.util.ArrayList;
import java.util.HashMap;
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
     * This method executes a query and returns a list of maps containing the id and
     * name of the query result.
     * 
     * @param queryString The query string to execute.
     * @param idField     The name of the id field in the query result.
     * @param nameField   The name of the name field in the query result.
     * @return A list of maps containing the id and name of the query result.
     */
    private List<Map<String, Object>> executeQuery(String queryString, String idField, String nameField) {
        List<Object[]> queryResult = entityManager.createQuery(queryString, Object[].class)
                .getResultList();

        return queryResult.stream()
                .map(row -> Map.of("id", row[0], nameField, row[1]))
                .collect(Collectors.toList());
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