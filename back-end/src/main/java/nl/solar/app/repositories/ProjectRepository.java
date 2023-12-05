package nl.solar.app.repositories;

import java.util.List;
import java.util.Map;

import nl.solar.app.models.Project;
import nl.solar.app.repositories.jpaRepositories.ProjectRepositoryJpa;

/**
 * Repository for the {@link Project} entity.
 * 
 * @see EntityRepository
 * @see Project
 * @see ProjectRepositoryJpa
 * 
 * @author Tim Knops
 */
public interface ProjectRepository extends EntityRepository<Project> {

    /**
     * Retrieves a list of all project names and ids.
     * 
     * @return A list of all projects.
     */
    List<Map<String, Object>> getTeamsInfo();

    /**
     * Retrieves a list of all product names and ids.
     * 
     * @return A list of all products.
     */
    List<Map<String, Object>> getProductsInfo();
}
