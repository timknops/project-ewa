package nl.solar.app.models;

import java.util.Objects;

public class Team {
    private long id;
    private String team;
    private Warehouse warehouse;
    private TeamType type;
    public Team(long id, String team, Warehouse warehouse, TeamType type) {
        this.id = id;
        this.team = team;
        this.warehouse = warehouse;
        this.type = type;
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
    public Warehouse getWarehouse() {return warehouse;}

    public void setWarehouse(Warehouse warehouse) {this.warehouse = warehouse;}

    public TeamType getType() {return type;}
    public void setType(TeamType type) {this.type = type;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

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
