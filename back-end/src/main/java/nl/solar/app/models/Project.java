package nl.solar.app.models;

import java.util.Date;

import nl.solar.app.enums.ProjectStatus;

/**
 * Represents a project with a unique id, project name, team, client, due date,
 * and status.
 * 
 * @author Tim Knops
 */
public class Project {

    private long id;
    private String projectName;
    private Team team;
    private String client;
    private Date dueDate;
    private ProjectStatus status;

    public Project(long id, String projectName, Team team, String client, Date dueDate, ProjectStatus status) {
        this.id = id;
        this.projectName = projectName;
        this.team = team;
        this.client = client;
        this.dueDate = dueDate;
        this.status = status;
    }

    /**
     * Creates a dummy project with the given parameters.
     * 
     * @param id          the id of the project
     * @param projectName the name of the project
     * @param team        the team of the project
     * @param client      the client of the project
     * @param dueDate     the due date of the project
     * @param status      the status of the project
     * @return a dummy project
     */
    public static Project createDummyProject(long id, String projectName, Team team, String client, Date dueDate,
            ProjectStatus status) {
        return new Project(id, projectName, team, client, dueDate, status);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Project project) {
            return this.getId() == project.id;
        }
        return false;
    }
}
