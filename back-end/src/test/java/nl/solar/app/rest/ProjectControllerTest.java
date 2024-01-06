
package nl.solar.app.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nl.solar.app.models.Project;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
}