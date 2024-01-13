package nl.solar.app.rest;

import nl.solar.app.DTO.TeamDTO;
import nl.solar.app.exceptions.BadRequestException;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Team;
import nl.solar.app.models.Warehouse;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {

    TeamRepository teamRepository;

    EntityRepository<Warehouse> warehouseEntityRepository;

    public TeamController(TeamRepository teamRepository, EntityRepository<Warehouse> warehouseEntityRepository) {
        this.teamRepository = teamRepository;
        this.warehouseEntityRepository = warehouseEntityRepository;
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable long id) {
        Team team = teamRepository.findById(id);
        if (team == null) {
            throw new ResourceNotFoundException("Team with id: '" + id + "' was not found");
        }

        TeamDTO teamDTO = convertToDTO(team);
        return ResponseEntity.ok(teamDTO);
    }

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody Team team) {
        if (team.getTeam() == null || team.getTeam().isBlank())
            throw new BadRequestException("Team name can't be empty");
        Warehouse warehouse = warehouseEntityRepository.findById(team.getWarehouse().getId());
        team.setWarehouse(warehouse);

        Team createdTeam = teamRepository.save(team);
        TeamDTO createdTeamDTO = convertToDTO(createdTeam);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTeamDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdTeamDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable long id, @RequestBody TeamDTO teamDTO) {
        if (teamDTO.getTeam() == null || teamDTO.getTeam().isBlank())
            throw new BadRequestException("Team name can't be empty");
        Team existingTeam = teamRepository.findById(id);
        if (existingTeam == null) {
            throw new ResourceNotFoundException("Team not found with id: " + id);
        }

        existingTeam.setTeam(teamDTO.getTeam());

        Warehouse warehouse = warehouseEntityRepository.findAll().stream()
                .filter(w -> w.getName().equals(teamDTO.getWarehouseName()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with name: " + teamDTO.getWarehouseName()));

        existingTeam.setWarehouse(warehouse);

        TeamDTO updatedTeamDTO = convertToDTO(teamRepository.save(existingTeam));
        return ResponseEntity.ok(updatedTeamDTO);
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

    private TeamDTO convertToDTO(Team team) {
        return new TeamDTO(team.getId(), team.getTeam(), team.getWarehouse().getName(), team.getType().name());
    }
}
