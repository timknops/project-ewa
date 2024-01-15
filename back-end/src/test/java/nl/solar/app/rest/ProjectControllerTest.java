
package nl.solar.app.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.solar.app.WebConfig;
import nl.solar.app.models.wrappers.ProjectRequestWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

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
        // Send HTTP GET request to the API endpoint
        ResponseEntity<List<Project>> response = restTemplate.exchange("/projects", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        // Check if the response is OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check if the body is not empty.
        assertNotNull(response.getBody());
    }
}