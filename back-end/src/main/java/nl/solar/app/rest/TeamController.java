package nl.solar.app.rest;

import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Team;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    EntityRepository<Team> teamRepository;

    EntityRepository<Warehouse> warehouseEntityRepository;

    public TeamController(EntityRepository<Team> teamRepository, EntityRepository<Warehouse> warehouseEntityRepository) {
        this.teamRepository = teamRepository;
        this.warehouseEntityRepository = warehouseEntityRepository;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable long id) {
        Team team = teamRepository.findById(id);
        if (team == null) {
            throw new ResourceNotFoundException("Team with id: '" + id + "' was not found");
        }
        return ResponseEntity.ok(team);
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        validateTeam(team);

        Warehouse warehouse = warehouseEntityRepository.findById(team.getWarehouse().getId());
        team.setWarehouse(warehouse);

        Team createdTeam = teamRepository.save(team);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTeam.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdTeam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable long id, @RequestBody Team updatedTeam) {
        Team existingTeam = teamRepository.findById(id);
        if (existingTeam == null) {
            throw new ResourceNotFoundException("Team not found with id: " + id);
        }

        validateTeam(updatedTeam);

        existingTeam.setTeam(updatedTeam.getTeam());
        Warehouse warehouse = warehouseEntityRepository.findById(updatedTeam.getWarehouse().getId());
        existingTeam.setWarehouse(warehouse);

        Team updatedTeamEntity = teamRepository.save(existingTeam);
        return ResponseEntity.ok(updatedTeamEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable long id) {
        Team existingTeam = teamRepository.findById(id);
        if (existingTeam == null) {
            throw new ResourceNotFoundException("Team not found with id: " + id);
        }

        if (!existingTeam.getProjects().isEmpty()) {
            throw new PreConditionFailedException("Team is being used and cannot be deleted");
        }

        teamRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validateTeam(Team team) {
        if (team.getTeam() == null || team.getTeam().isBlank()) {
            throw new BadRequestException("Team name can't be empty");
        }
    }
}
