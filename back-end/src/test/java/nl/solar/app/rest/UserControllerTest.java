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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Various tests for the UserController
 *
 * @author Noa de Greef
 */
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

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK,
                "Response status should be equal to HttpStatus.OK");
        User[] users = responseEntity.getBody();

        assertThat(users.length, is(greaterThan(0)));
    }

    @Test
    public void returnsSpecificUserBasedOnGivenId(){
        ResponseEntity<User> responseEntity = this.testRestTemplate.getForEntity("/users/5000", User.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK,
                "Response status should be equal to HttpStatus.OK");
        User user = responseEntity.getBody();
        assertEquals(user.getId(),
                5000, "Id must be equal to that of the called value: 5000");
        assertEquals(user.getName(), "Julian",
                "Name must be equal to that of the expected user name value 'Julian'");
        assertEquals(user.getEmail(), "greefnhva@gmail.com",
                "Email must be equal to that of the expected user email value 'greefnhva@gmail.com!'");
    }

    @Test
    public void aNewUserCanBePosted() {
        //get a team to create a new user with
        ResponseEntity<User> responseEntity = this.testRestTemplate.getForEntity("/users/5000", User.class);
        assertNotNull(responseEntity.getBody().getTeam(),
                "User should have been found and therefore not be null");

        //create a new user with an existing team
        User newUser = new User(0, responseEntity.getBody().getTeam(),
                "Tommy", "tommy@gmail.com", "password123", "VIEWER");

        //post the new user
        ResponseEntity<User> postResponseEntity =
                this.testRestTemplate.postForEntity("/users", newUser, User.class);
        assertEquals(postResponseEntity.getStatusCode(), HttpStatus.CREATED,
                "User post status should be equal to HttpStatus.Created");

        //put the response of the post body into a new user and test the values
        User addedUser = postResponseEntity.getBody();
        assertTrue(addedUser.getId() > 5000,
                "Id should be greater than 5000, since that is the initial value");
        assertEquals(addedUser.getName(), newUser.getName(),
                "Name must be equal to the name of the user that was created at the start of this test");
        assertEquals(addedUser.getEmail(), newUser.getEmail(),
                "Email must be equal to the email that was created at the start of this test");

        //Try to get the newly added user from the database based on the id
        ResponseEntity<User> retryResponseEntity =
                this.testRestTemplate.getForEntity("/users/" + addedUser.getId(), User.class);
        assertEquals(retryResponseEntity.getStatusCode(), HttpStatus.OK,
                "User get status after adding should be equal to HttpStatus.OK");

        //put the response body from the back-end into a new user and test it on the values
        User createdUser = retryResponseEntity.getBody();
        assertTrue(createdUser.getId() > 5000,
                "Created user id should be greater than 5000, since that is the initial value");
        assertEquals(createdUser.getName(), newUser.getName(),
                "Created user name must be equal to the name of the user that was created at the start of this test");
        assertEquals(createdUser.getEmail(), newUser.getEmail(),
                "Created user email must be equal to the email that was created at the start of this test");
    }
}
