package nl.solar.app.repositories;

import nl.solar.app.models.Team;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("TEAMS.INMEMORY")
public class TeamRepositoryMock implements EntityRepository<Team> {
    private List<Team> teams;
    private long currentId = 1;

    public TeamRepositoryMock() {
        teams = new ArrayList<>();
        teams.add(new Team(currentId++, "Team 1", Team.Warehouse.SolarSedum, Team.TeamType.Internal));
        teams.add(new Team(currentId++, "Team 2", Team.Warehouse.SolarSedum, Team.TeamType.Internal));
        teams.add(new Team(currentId++, "Team 3", Team.Warehouse.SolarSedum, Team.TeamType.Internal));
        teams.add(new Team(currentId++, "Team 2", Team.Warehouse.EHES, Team.TeamType.External));
        teams.add(new Team(currentId++, "Team 2", Team.Warehouse.Superzon, Team.TeamType.External));
    }

    @Override
    public List<Team> findALL() {
        return teams;
    }

    @Override
    public Team findById(long id) {
        return teams.stream().filter(team -> team.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Team delete(long id) {
        Team deleteTeam = this.findById(id);

        if (deleteTeam != null) {
            teams.remove(deleteTeam);
        }
        return deleteTeam;
    }

    @Override
    public Team save(Team item) {
        if (item.getWarehouse() == Team.Warehouse.SolarSedum) {
            item.setType(Team.TeamType.Internal);
        } else {
            item.setType(Team.TeamType.External);
        }

        int index = teams.indexOf(item);
        if (index != -1) {
            teams.set(index, item);
        } else {
            if (item.getId() == 0) {
                item.setId(currentId);
            }
            teams.add(item);
        }
        return item;
    }
}
