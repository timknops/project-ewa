package nl.solar.app.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import nl.solar.app.enums.ProjectStatus;

/**
 * Represents a project with a unique id, project name, team, client, due date,
 * and status.
 * 
 * @author Tim Knops
 */
@Entity
public class Project {

    @Id
    private long id;

    private String projectName;

    @OneToOne
    private Team team;
    private String client;
    private Date dueDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToOne
    private Warehouse warehouse;

    @OneToMany(mappedBy = "project")
    private List<ResourceTemp> products;

    public Project(long id, String projectName, Team team, String client, Date dueDate, ProjectStatus status,
            Warehouse warehouse, List<ResourceTemp> products) {
        this.id = id;
        this.projectName = projectName;
        this.team = team;
        this.client = client;
        this.dueDate = dueDate;
        this.status = status;
        this.warehouse = warehouse;
        this.products = products;
    }

    /**
     * Creates a dummy project with the given parameters.
     * 
     * @param id          the id of the project
     * @param projectName the name of the project
     * @param team        the team of the project
     * @param client      the client of the project
     * @return a dummy project
     */
    public static Project createDummyProject(long id, String projectName, Team team, String client, Warehouse warehouse,
            List<Product> products) {
        // Generates a random date between 2022-01-01 and 2026-01-01.
        Date randomDueDate = randomDate(new Date(1640995200000L), new Date(1789568000000L));

        ProjectStatus randomStatus;
        if (randomDueDate.before(new Date())) { // If the due date is in the past, the project is completed.
            randomStatus = ProjectStatus.COMPLETED;
        } else { // If the due date is in the future, the project is either upcoming or in
                 // progress.
            double random = Math.random();

            // 40% chance of being upcoming.
            randomStatus = random < 0.4 ? ProjectStatus.UPCOMING : ProjectStatus.IN_PROGRESS;
        }

        return new Project(id, projectName, team, client, randomDueDate, randomStatus, warehouse, products);
    }

    /**
     * Generates a random date between the given start and end date.
     * 
     * @param start the start date
     * @param end   the end date
     * @return a random date between the given start and end date
     */
    public static Date randomDate(Date start, Date end) {
        return new Date(start.getTime() + (long) (Math.random() * (end.getTime() - start.getTime())));
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

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
