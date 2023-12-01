package nl.solar.app.repositories;

import java.util.List;

import nl.solar.app.DTO.ProjectResourceDTO;
import nl.solar.app.models.Resource;

public interface ResourceRepository extends ManyToManyRepository<Resource> {

    /**
     * Get all resources from a project
     * 
     * @param projectId the id of the project
     * @return a map with the product name as key and the quantity as value
     */
    List<ProjectResourceDTO> getProjectResources(long projectId);
}
