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

    /**
     * Finds all projects by team id.
     * 
     * @param teamId The id of the team to find projects for.
     * @return A list of all projects for the given team.
     */
    List<Project> findByTeamId(long teamId);
}
