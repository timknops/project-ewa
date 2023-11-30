package nl.solar.app.models.wrappers;

import nl.solar.app.models.Project;

/**
 * The ProjectRequestWrapper class represents a wrapper object that contains a
 * Project object and a team ID.
 * It is used to encapsulate the data required for creating or updating a
 * project.
 * 
 * @see Project
 * @author Tim Knops
 */
public class ProjectRequestWrapper {

  private Project project;
  private long teamId;

  /**
   * Constructs a new ProjectRequestWrapper object with the specified Project and
   * team ID.
   * 
   * @param project The Project object.
   * @param teamId  The team ID.
   */
  public ProjectRequestWrapper(Project project, long teamId) {
    this.project = project;
    this.teamId = teamId;
  }

  /**
   * Constructs a new empty ProjectRequestWrapper object.
   */
  public ProjectRequestWrapper() {
  }

  /**
   * Returns the Project object.
   * 
   * @return The Project object.
   */
  public Project getProject() {
    return project;
  }

  /**
   * Returns the team ID.
   * 
   * @return The team ID.
   */
  public long getTeamId() {
    return teamId;
  }

  /**
   * Sets the Project object.
   * 
   * @param project The Project object.
   */
  public void setProject(Project project) {
    this.project = project;
  }

  /**
   * Sets the team ID.
   * 
   * @param teamId The team ID.
   */
  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

}