package nl.solar.app.repositories;

import nl.solar.app.models.Team;
import nl.solar.app.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = {Repository.class, Entity.class}), showSql = false)
public class UserRepositoryTest {

    @Qualifier("USER.JPA")
    @Autowired
    private EntityRepository<User> userEntityRepository;

    @Autowired
    CommandLineRunner commandLineRunner;

    private List<User> userList;

    @BeforeAll
    public static void classLevelSetup() {
        System.out.println("Setting up UserRepositoryTest");
    }

    @BeforeEach
    public void setup() throws Exception {
        this.commandLineRunner.run(null);
        this.userList = this.userEntityRepository.findAll();
    }

    @Test
    public void FindAllReturnsAllUsers() {
        assertTrue(this.userList.size() > 0);
    }

    @Test
    public void FindByIdReturnsSingleUserById() {
        for (User value : this.userList) {
            User user = userEntityRepository.findById(value.getId());
            assertEquals(value.getId(), user.getId(), "Id should be equal");
            assertEquals(value.getTeam(), user.getTeam(), "Team should be equal");
            assertEquals(value.getName(), user.getName(), "Name should be equal");
            assertEquals(value.getEmail(), user.getEmail(), "Email address should be equal");
            assertEquals(value.getPassword(), user.getPassword(), "Password should be equal");
            assertEquals(value.getType(), user.getType(), "Type should be equal");
        }
    }

    @Test
    public void DeleteDeletesASpecificUser() {
        //get first user in list
        User user = userList.get(0);
        //delete the user
        User deletedUser = userEntityRepository.delete(user.getId());
        //try to delete the same user that was deleted earlier
        User userRetest = userEntityRepository.findById(user.getId());
        //delete a user that should not exist
        User notExistentDeletedUser = userEntityRepository.delete(9999999);

        assertEquals(user, deletedUser, "Deleted user should be equal to user that was called to be deleted");
        assertNull(userRetest, "User should have  been deleted and therefore be null");
        assertNull(notExistentDeletedUser, "No user should have been deleted and therefore be null");
    }

    @Test
    public void SaveUpdatesAUser() {
        //get a user
        User user = userList.get(0);
        assertEquals(user.getName(), "Julian",
                "Name of user should be equal to expected value 'Julian'");
        assertEquals(user.getEmail(), "greefnhva@gmail.com",
                "Email of user should be equal to expected value 'greefnhva@gmail.com' ");

        //modify the user
        user.setName("Tommy");
        user.setEmail("tommy@gmail.com");
        //update the modified user
        User addedUser = userEntityRepository.save(user);

        assertEquals(addedUser, user, "Updated user should be equal to the newly updated user");
        assertEquals(addedUser.getName(),"Tommy",
                "Updated name of user should be equal to new expected value 'Tommy'");
        assertEquals(addedUser.getEmail(), "tommy@gmail.com",
                "Updated email of user should be equal to expected value 'tommy@gmail.com'");
    }

    @Test
    public void SaveAddsAUser() {
        Team team = userList.get(0).getTeam();
        User user = new User(
                7777, team, "Peter", "Peter@gmail.com", "password123", "VIEWER");

        User addedUser = userEntityRepository.save(user);
        assertEquals(addedUser, user, "Added user should be equal to the newly created user");
        assertNotEquals(userEntityRepository.findAll().size(), userList.size(),
                "User sizes should not be equal since a user should have been added");
    }
}
