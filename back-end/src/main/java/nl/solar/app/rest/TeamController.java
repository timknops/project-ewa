package nl.solar.app.rest;

import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Team;
import nl.solar.app.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    TeamRepository teamRepo;

    @GetMapping(produces = "application/json")
    public List<Team> getAll() {
        return this.teamRepo.findAll();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) throws ResourceNotFoundException {
        Team team = this.teamRepo.findById(id);

        if (team == null) {
            throw new ResourceNotFoundException("Team with id: " + id + " was not found");
        }
        return ResponseEntity.ok(team);
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Team> deleteTeam(@PathVariable long id) throws ResourceNotFoundException {
        Team deleted = this.teamRepo.delete(id);

        if (deleted == null) {
            throw new ResourceNotFoundException("Cannot delete team with id: " + id + " Team not found");
        }

        return ResponseEntity.ok(deleted);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Team> addTeam(@RequestBody Team team) throws BadRequestException {
        if (team.getTeam() == null || team.getTeam().isBlank()) throw new BadRequestException("Team name can't be empty");
        Team added = this.teamRepo.save(team);


        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(added.getId()).toUri();
        return ResponseEntity.created(location).body(team);
    }

    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Team> updateTeam(@PathVariable long id, @RequestBody Team team)
            throws PreConditionFailedException, BadRequestException {
        if (id != team.getId()) throw new PreConditionFailedException("Id of the body and path do not match");
        if (team.getTeam() == null || team.getTeam().isBlank()) throw new BadRequestException("Team name can't be empty");

        Team updated = this.teamRepo.save(team);
        return ResponseEntity.ok(updated);
    }

    @GetMapping(path = "/modal", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAddModalInfo() throws ResourceNotFoundException {
        List<Map<String, Object>> teamsInfo = this.teamRepo.getWarehousesInfo();

        Map<String, Object> response = new HashMap<>();
        response.put("warehouses", teamsInfo);

        return ResponseEntity.ok(response);
    }
}
