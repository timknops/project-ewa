
package nl.solar.app.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nl.solar.app.enums.ProjectStatus;
import nl.solar.app.models.Project;
import nl.solar.app.models.Team;
import nl.solar.app.repositories.ProjectRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProjectRepository projectRepo;

    @Test
    public void testGetAll() {
        // Send HTTP GET request to the API endpoint.
        ResponseEntity<List<Project>> response = restTemplate.exchange("/projects", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Project>>() {
                });

        // Check if the response is OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check if the body is not empty.
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetProject() {
        // Create a test project
        Project testProject = new Project();
        testProject.setId(1L);
        testProject.setProjectName("Test Project");
        testProject.setStatus(ProjectStatus.IN_PROGRESS);
        testProject.setDueDate(new Date());
        testProject.setClient("Test Client");
        testProject.setTeam(new Team(1L));
        testProject.setDescription("Test Description");

        // Save the test project to the repository
        projectRepo.save(testProject);

        // Send HTTP GET request to the API endpoint with the test project ID
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/projects/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        // Check if the response is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check if the body is not empty
        assertNotNull(response.getBody());

        // Verify the response body contains the expected project details
        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null) {
            throw new AssertionError("Response body is null");
        }

        assertEquals(1, responseBody.get("id"));
        assertEquals("Test Project", responseBody.get("projectName"));
        assertEquals(ProjectStatus.IN_PROGRESS, ProjectStatus.valueOf((String) responseBody.get("status")));

        // Format the expected date in the same way as the actual date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String expectedDueDate = dateFormat.format(new Date());

        assertEquals(expectedDueDate, responseBody.get("dueDate").toString());

        assertEquals("Test Client", responseBody.get("client"));
        assertEquals(1, responseBody.get("team"));
        assertEquals("Test Description", responseBody.get("description"));
    }

}