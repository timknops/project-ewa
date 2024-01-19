package nl.solar.app.repositories;

import jakarta.persistence.Entity;
import nl.solar.app.models.Team;
import nl.solar.app.models.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TeamRepositoryJpa.
 * This class tests the TeamRepositoryJpa by testing all methods.
 *
 * @author Nashon Woldai
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = { Repository.class, Entity.class }), showSql = false)
public class TeamRepositoryTest {

    @Autowired
    CommandLineRunner commandLineRunner;

    @Qualifier("TEAMS.JPA")
    @Autowired
    private EntityRepository<Team> teamEntityRepository;

    @Autowired
    EntityRepository<Warehouse> warehouseEntityRepository;

    private List<Team> teams;

    // Setup method to run the command line runner and fetch teams before each test
    @BeforeEach
    public void setup() throws Exception {
        commandLineRunner.run(null);
        teams = teamEntityRepository.findAll();
    }

    // Test case to ensure that the findAllTeams method returns a non-empty list
    @Test
    public void testFindAllTeams() {
        assertFalse(teams.isEmpty());
    }

    // Test case to ensure that teams can be saved and retrieved by ID
    @Test
    public void testSaveAndFindById() {
        // Fetching a warehouse for team creation
        Warehouse warehouse = warehouseEntityRepository.findAll().get(0);

        // Creating a new team
        Team team = new Team();
        team.setTeam("Test Team");
        team.setWarehouse(warehouse);
        team.setType(Team.TeamType.INTERNAL);

        // Saving the team and fetching it by ID
        Team savedTeam = teamEntityRepository.save(team);

        // Verifying that the saved team is retrieved successfully
        Team foundTeam = teamEntityRepository.findById(savedTeam.getId());
        assertNotNull(foundTeam, "Saved team should be found");
        assertEquals("Test Team", foundTeam.getTeam(), "Team name should match");
    }
}
