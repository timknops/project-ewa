package nl.solar.app.models.wrappers;

import java.util.List;

import nl.solar.app.DTO.ProjectResourceDTO;
import nl.solar.app.models.Project;

/**
 * Represents a project request wrapper.
 * 
 * @see Project
 * @author Tim Knops
 */
public class ProjectRequestWrapper {

  private Project project;
  private List<ProjectResourceDTO> resources;

  /**
   * Creates a ProjectRequestWrapper with a project and a team ID.
   * 
   * @param project - the project
   * @param teamId  - the team ID
   */

  public ProjectRequestWrapper(Project project, List<ProjectResourceDTO> resources) {
    this.project = project;
    this.resources = resources;
  }

  public ProjectRequestWrapper() {
  }

  public Project getProject() {
    return project;
  }

  public List<ProjectResourceDTO> getResources() {
    return resources;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public void setResources(List<ProjectResourceDTO> resources) {
    this.resources = resources;
  }

}