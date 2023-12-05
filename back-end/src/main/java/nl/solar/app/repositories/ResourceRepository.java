package nl.solar.app.repositories;

import java.util.List;

import nl.solar.app.DTO.ProjectResourceDTO;
import nl.solar.app.models.Resource;

/**
 * Repository for the {@link Resource} entity.
 * 
 * @author Tim Knops
 */
public interface ResourceRepository extends ManyToManyRepository<Resource> {

    /**
     * Get all resources from a project
     * 
     * @param projectId the id of the project
     * @return a map with the product name as key and the quantity as value
     */
    List<ProjectResourceDTO> getProjectResources(long projectId);

    /**
     * Add resources to a project
     * 
     * @param projectId the id of the project
     * @param resources the resources to add
     */
    void addProjectResources(long projectId, List<ProjectResourceDTO> resources);

    /**
     * Delete all resources from a project
     * 
     * @param projectId the id of the project
     */
    void deleteProjectResources(long projectId);
}
