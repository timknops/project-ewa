package nl.solar.app.DTO;

public class TeamDTO {
    private long id;
    private String team;
    private String warehouseName;
    private String type;

    public TeamDTO(){

    }

    public TeamDTO(long id, String team, String warehouseName, String type) {
        this.id = id;
        this.team = team;
        this.warehouseName = warehouseName;
        this.type = type;
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

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
