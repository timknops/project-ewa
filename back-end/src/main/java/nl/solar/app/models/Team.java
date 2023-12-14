package nl.solar.app.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import nl.solar.app.models.views.ProjectView;

@Entity
public class Team {

    @Id
    @SequenceGenerator(name = "team_id_generator", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_generator")
    @JsonView(ProjectView.Overview.class)
    private long id;

    @JsonView(ProjectView.Overview.class)
    private String team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    @JsonView(ProjectView.Overview.class)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private TeamType type;

    @OneToMany(mappedBy = "team")
    private Set<Project> projects = new HashSet<>();

    public Team(long id, String team, Warehouse warehouse, TeamType type) {
        this.id = id;
        this.team = team;
        this.warehouse = warehouse;
        this.type = type;
    }

    public Team(long id) {
        this.id = id;
    }

    public Team() {
    }

    public static Team createDummyTeam(Warehouse warehouse, String teamName, TeamType teamType) {
        Team team = new Team();
        team.setWarehouse(warehouse);
        team.setTeam(teamName);
        team.setType(teamType);
        return team;
    }

    public enum TeamType {
        Internal, External
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public TeamType getType() {
        return type;
    }

    public void setType(TeamType type) {
        this.type = type;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> project) {
        this.projects = project;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Team team) {
            return this.getId() == team.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}