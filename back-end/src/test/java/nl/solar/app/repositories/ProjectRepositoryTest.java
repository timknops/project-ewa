package nl.solar.app.repositories;

import jakarta.persistence.Entity;
import nl.solar.app.enums.ProjectStatus;
import nl.solar.app.models.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ProjectRepository.
 * 
 * @see ProjectRepository
 * @see Project
 * @author Tim Knops
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Repository.class,
        Entity.class }))
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private List<Project> projectList;

    @Test
    public void findAllShouldReturnAllProjects() {
        projectList = projectRepository.findAll();
        assertFalse(projectList.isEmpty(), "Project list should not be empty");
    }

    @Test
    public void findByIdShouldReturnSpecificProject() {
        projectList = projectRepository.findAll();
        assertFalse(projectList.isEmpty(), "Project list should not be empty");

        // Loop through all projects and check if the project found by id is the same as
        // the one in the list.
        for (Project project : projectList) {
            Project foundProject = projectRepository.findById(project.getId());
            assertEquals(project, foundProject, "Project should be the same as the one found by id");
        }
    }

    @Test
    public void deleteShouldRemoveProject() {
        // Create a new project, and delete it.
        Project project = new Project();
        project.setProjectName("TestProject");
        project.setClient("TestClient");
        project.setStatus(ProjectStatus.UPCOMING);
        project.setDescription("TestDescription");

        // Save the project, find it by id, and check if it is the same as the one
        // saved.
        projectRepository.save(project);
        Project foundProject = projectRepository.findById(project.getId());

        assertEquals(project, foundProject, "Project should be the same as the one found by id");

        // Delete the project, find it by id, and check if it is null.
        projectRepository.delete(project.getId());
        foundProject = projectRepository.findById(project.getId());

        assertNull(foundProject, "Project should be null after deletion");
    }

    @Test
    public void saveShouldAddProject() {
        Project project = new Project();
        project.setProjectName("TestProject");
        project.setClient("TestClient");
        project.setStatus(ProjectStatus.UPCOMING);
        project.setDescription("TestDescription");

        projectRepository.save(project);
        Project foundProject = projectRepository.findById(project.getId());
        assertEquals(project, foundProject, "Project should be the same as the one found by id");
    }

    @Test
    public void getTeamsInfoShouldReturnListOfMaps() {
        List<?> teamsInfo = projectRepository.getTeamsInfo();

        assertFalse(teamsInfo.isEmpty(), "Teams info list should not be empty");
        assertInstanceOf(Map.class, teamsInfo.get(0), "Teams info list should contain maps");

        // Loop over all teams, and check if each map contains a 'team' and 'id'.
        for (Object teamInfo : teamsInfo) {
            Map<?, ?> teamInfoMap = (Map<?, ?>) teamInfo;
            assertTrue(teamInfoMap.containsKey("team"), "Map should contain team");
            assertTrue(teamInfoMap.containsKey("id"), "Map should contain id");
        }
    }

    @Test
    public void getProductsInfoShouldReturnListOfMaps() {
        List<?> productsInfo = projectRepository.getProductsInfo();

        assertFalse(productsInfo.isEmpty(), "Products info list should not be empty");
        assertInstanceOf(Map.class, productsInfo.get(0), "Products info list should contain maps");

        // Loop over all products, and check if each map contains a 'product_name' and
        // 'id'.
        for (Object productInfo : productsInfo) {
            Map<?, ?> productInfoMap = (Map<?, ?>) productInfo;
            assertTrue(productInfoMap.containsKey("product_name"), "Map should contain product_name");
            assertTrue(productInfoMap.containsKey("id"), "Map should contain id");
        }
    }

    @Test
    public void findByTeamIdShouldReturnListOfProjects() {
        List<Project> projects = projectRepository.findByTeamId(1);

        assertFalse(projects.isEmpty(), "Projects list should not be empty");
        assertInstanceOf(Project.class, projects.get(0), "Projects list should contain projects");

        // Loop over all projects and check if the team id is the same as the one given.
        for (Project project : projects) {
            assertEquals(1, project.getTeam().getId(), "Team id should be the same as the one given");
        }
    }

    @Test
    public void findByTeamIdShouldReturnEmptyListWhenNoProjectsForTeam() {
        List<Project> projects = projectRepository.findByTeamId(9999);
        assertTrue(projects.isEmpty(), "Projects list should be empty when no projects for team");
    }

    @Test
    public void getTeamsInfoShouldNotContainOtherKeys() {
        List<?> teamsInfo = projectRepository.getTeamsInfo();
        for (Object teamInfo : teamsInfo) {
            Map<?, ?> teamInfoMap = (Map<?, ?>) teamInfo;
            assertEquals(2, teamInfoMap.size(), "Map should only contain 'team' and 'id'");
        }
    }

    @Test
    public void getProductsInfoShouldNotContainOtherKeys() {
        List<?> productsInfo = projectRepository.getProductsInfo();
        for (Object productInfo : productsInfo) {
            Map<?, ?> productInfoMap = (Map<?, ?>) productInfo;
            assertEquals(2, productInfoMap.size(), "Map should only contain 'product_name' and 'id'");
        }
    }
}
