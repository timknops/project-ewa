package nl.solar.app.rest;

import nl.solar.app.WebConfig;
import nl.solar.app.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    WebConfig webConfig;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${server.servlet.context-path}")
    public String servletContextPath;

    @BeforeEach
    public void setup() {
        //currently /users is not actually protected by SECURED_PATHS, but in the future this should get changed.
        //So to building with the future in mind, change the secure paths so that authentication is not needed
        webConfig.SECURED_PATHS = Set.of("/empty");
        if (servletContextPath == null){
            servletContextPath = "/";
        }
    }

    @Test
    public void returnsAllUsers(){
        ResponseEntity<User[]> responseEntity = this.testRestTemplate.getForEntity("/users", User[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User[] users = responseEntity.getBody();
        assertThat(users.length, is(greaterThan(0)));
    }
}
