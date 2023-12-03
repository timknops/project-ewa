package nl.solar.app.repositories;

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
     * Retrieves the information needed to display the add modal.
     * 
     * @return The information needed to display the add modal.
     */
    Object getAddModalInfo();
}
