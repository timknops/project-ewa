package nl.solar.app.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.User;
import nl.solar.app.models.views.UserView;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    EntityRepository<User> userRepo;

    @GetMapping(produces = "application/json")
    public List<User> getTestUsers() {
        return userRepo.findAll();
    }

    /**
     * Custom view for the admin, which excludes passwords
     */
    @JsonView(UserView.userAdmin.class)
    @GetMapping("/admin")
    public List<User> getAdminUsers() {
        return userRepo.findAll();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable long id) throws ResourceNotFoundException {
        User user = userRepo.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Cannot find a user with id = " + id);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<User> saveScooter(@RequestBody User userParameter) throws BadRequestException {
        if (userParameter.getName() == null || userParameter.getName().isBlank())
            throw new BadRequestException("Name cannot be empty");
        User user = this.userRepo.save(userParameter);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(user);
    }

    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User userParameter)
            throws PreConditionFailedException, BadRequestException {
        if (id != userParameter.getId())
            throw new PreConditionFailedException("The id of the user is not the same as the id of the url");
        if (userParameter.getName() == null || userParameter.getName().isBlank())
            throw new BadRequestException("Name can't be empty");

        User user = this.userRepo.save(userParameter);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<User> deleteUser(@PathVariable long id) throws ResourceNotFoundException {
        User deletedUser = this.userRepo.delete(id);

        if (deletedUser == null) {
            throw new ResourceNotFoundException("The user for this id doesn't exist");
        }

        return ResponseEntity.ok(deletedUser);
    }
}
