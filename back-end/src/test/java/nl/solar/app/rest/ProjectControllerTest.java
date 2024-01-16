
package nl.solar.app.rest;

import java.text.SimpleDateFormat;
import java.util.*;

import nl.solar.app.WebConfig;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.wrappers.ProjectRequestWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nl.solar.app.enums.ProjectStatus;
import nl.solar.app.models.Project;
import nl.solar.app.models.Team;
import nl.solar.app.repositories.ProjectRepository;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectControllerTest {

    @Autowired
    private WebConfig webConfig;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    @BeforeEach
    public void setup() {
        webConfig.SECURED_PATHS = Set.of("/empty"); //changes the webconfig, so that the test don't need authentication
        if (servletContextPath == null) {
            servletContextPath = "/";
        }
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void shouldReturnAllProjectsWhenGetAllIsCalled() {
        ResponseEntity<List<Project>> response = restTemplate.exchange("/projects", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        // Check if the response is OK.
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 'OK'");

        // Check if the body is not empty.
        assertNotNull(response.getBody(), "Body should not be null");
    }

    @Test
    public void shouldDeleteProjectWhenCalledWithValidId() {
        ResponseEntity<Project> response = restTemplate.exchange("/projects/1", HttpMethod.DELETE, null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 'OK'");
        assertNotNull(response.getBody(), "Body should not be null");
    }

    @Test
    public void shouldReturnErrorWhenGetProjectIsCalledWithInvalidId() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/projects/9999", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        // Response should be OK, because of front-end handling.
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 'OK'");

        // Check if the body contains an error.
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("error"), "Body should contain an error");
    }

    @Test
    public void shouldReturnProjectsWhenGetProjectsForTeamIsCalledWithValidTeamId() {
        ResponseEntity<List<Project>> response = restTemplate.exchange("/projects/team/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        // Check if the response is OK.
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 'OK'");

        // Check if the body is not empty.
        assertNotNull(response.getBody(), "Body should not be null");
    }

    @Test
    public void shouldCreateProjectWhenValidRequestIsGiven() {
        // Get a team.
        ResponseEntity<List<Team>> teamResponse = restTemplate.exchange("/teams", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        if (Objects.requireNonNull(teamResponse.getBody()).isEmpty()) {
            fail("No teams found");
        }

        // Create a project.
        Project project = new Project();
        project.setProjectName("Test project");
        project.setClient("Test client");
        project.setDueDate(new Date());
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setTeam(teamResponse.getBody().get(0));

        // Create a new projectWrapper, used for the request.
        ProjectRequestWrapper projectWrapper = new ProjectRequestWrapper(project, null);

        // Create a request.
        HttpEntity<ProjectRequestWrapper> request = new HttpEntity<>(projectWrapper);

        // Send the request.
        ResponseEntity<Project> response = restTemplate.exchange("/projects", HttpMethod.POST, request,
                new ParameterizedTypeReference<>() {
                });

        // Check if the response is OK.
        Project createdProject = response.getBody();

        // Assert that the project is not null.
        assert createdProject != null;

        // Assertions for the created project.
        assertEquals("Test project", createdProject.getProjectName(), "Project name should be 'Test project'");
        assertEquals("Test client", createdProject.getClient(), "Client should be 'Test client'");
        assertEquals(ProjectStatus.IN_PROGRESS, createdProject.getStatus(), "Status should be 'IN_PROGRESS'");
        assertEquals(teamResponse.getBody().get(0).getId(), createdProject.getTeam().getId(), "Team id should be the same as the one given in the request");

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code should be 'CREATED'");
        assertNotNull(response.getBody(), "Body should not be null");
    }

    @Test
    public void shouldReturnErrorWhenCreateProjectIsCalledWithInvalidTeamId() {
        // Create a project.
        Project project = new Project();
        project.setProjectName("Test project");
        project.setClient("Test client");
        project.setDueDate(new Date());
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setTeam(new Team());

        // Create a new projectWrapper, used for the request.
        ProjectRequestWrapper projectWrapper = new ProjectRequestWrapper(project, null);

        // Create a request.
        HttpEntity<ProjectRequestWrapper> request = new HttpEntity<>(projectWrapper);

        // Send the request.
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/projects", HttpMethod.POST, request,
                new ParameterizedTypeReference<>() {
                });

        // Check if the response is not found.
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be 'NOT_FOUND'");

        // Check if the body contains an error.
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("error"), "Body should contain an error");
    }
}