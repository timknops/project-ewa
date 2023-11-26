package nl.solar.app.repositories;

import nl.solar.app.models.Project;

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

  Object getAddModalInfo();
}
