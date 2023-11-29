package nl.solar.app.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Team {

    @Id
    @SequenceGenerator(name = "team_id_generator", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_generator")
    private long id;

    private String team;
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private TeamType type;

    @OneToMany(mappedBy = "team")
    @JsonBackReference
    private List<Project> projects = new ArrayList<>();

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

    public static Team createDummyTeam() {
        String randomTeamName = "Team " + (int) (Math.random() * 100);
        return new Team(0, randomTeamName, Warehouse.SolarSedum, TeamType.Internal);
    }

    public enum Warehouse {
        SolarSedum, Superzon, Theswitch, Induct, EHES
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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> project) {
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
