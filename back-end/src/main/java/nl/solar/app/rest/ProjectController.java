package nl.solar.app.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Project;
import nl.solar.app.models.Team;
import nl.solar.app.models.views.ProjectView;
import nl.solar.app.models.wrappers.ProjectRequestWrapper;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.ProjectRepository;
import nl.solar.app.repositories.ResourceRepository;

/**
 * Controller class for managing project-related endpoints.
 * This class handles CRUD operations for projects.
 * 
 * @Author Tim Knops
 */
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    ResourceRepository resourceRepo;

    @Autowired
    EntityRepository<Team> teamRepo;

    /**
     * Retrieves a list of all projects specifically for the overview of the
     * projects.
     * 
     * @return A list of all projects.
     */
    @JsonView(ProjectView.Overview.class)
    @GetMapping(produces = "application/json")
    public List<Map<String, Object>> getAll() {
        List<Project> projects = this.projectRepo.findAll(); // Get all projects from the database.

        // Format the projects to a list of maps.
        List<Map<String, Object>> formattedProjects = new ArrayList<>();
        for (Project project : projects) { // Loop through all projects.
            Map<String, Object> formattedProject = new HashMap<>(); // Create a new map for the current project.
            formattedProject.put("id", project.getId());
            formattedProject.put("projectName", project.getProjectName());
            formattedProject.put("client", project.getClient());
            formattedProject.put("dueDate", project.getDueDate());
            formattedProject.put("status", project.getStatus());

            formattedProject.put("teamId", project.getTeam().getId());
            formattedProject.put("teamName", project.getTeam().getTeam());

            formattedProjects.add(formattedProject); // Add the formatted project to the list of formatted projects.
        }

        return formattedProjects;
    }

    /**
     * Retrieves a project by its id.
     *
     * @param id The id of the project to retrieve.
     * @return A ResponseEntity containing the retrieved project.
     * @throws ResourceNotFoundException If the project with the given ID does not
     *                                   exist.
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Project> getProject(@PathVariable Long id) throws ResourceNotFoundException {
        Project project = this.projectRepo.findById(id);

        // If the project doesn't exist, throw an error.
        if (project == null) {
            throw new ResourceNotFoundException("Project with id: " + id + " was not found");
        }

        return ResponseEntity.ok(project);
    }

    /**
     * Deletes a project by its id.
     *
     * @param id The id of the project to delete.
     * @return A ResponseEntity containing the deleted project.
     * @throws ResourceNotFoundException If the project with the given ID does not
     *                                   exist.
     */
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Project> deleteProject(@PathVariable Long id) throws ResourceNotFoundException {
        Project project = this.projectRepo.delete(id);

        // If the project doesn't exist, throw an error.
        if (project == null) {
            throw new ResourceNotFoundException("Project with id: " + id + " was not found");
        }

        return ResponseEntity.ok(project);
    }

    /**
     * Creates a new project.
     *
     * @param projectRequest The project to create.
     * @return A ResponseEntity containing the created project.
     * @throws ResourceNotFoundException If the project with the given ID does not
     */
    @PostMapping(path = "/add", produces = "application/json")
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequestWrapper projectRequest)
            throws ResourceNotFoundException {
        Project project = projectRequest.getProject();
        if (project == null) {
            throw new ResourceNotFoundException("Project is null");
        }

        // Get the team from the database.
        Team team = this.teamRepo.findById(projectRequest.getTeamId());
        if (team == null) {
            throw new ResourceNotFoundException("Team with id: " + projectRequest.getTeamId() + " was not found");
        }

        // Set the team for the project.
        project.setTeam(team);

        Project createdProject = this.projectRepo.save(project);

        // Add the project to the team.
        team.getProjects().add(createdProject);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProject.getId()).toUri();

        return ResponseEntity.created(location).body(createdProject);
    }

    /**
     * Updates a project by its id.
     *
     * @param id      The id of the project to update.
     * @param project The project to update.
     * @return A ResponseEntity containing the updated project.
     * @throws PreConditionFailedException If the project with the given ID does not
     *                                     exist.
     */
    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project)
            throws PreConditionFailedException {
        if (id != project.getId()) {
            throw new PreConditionFailedException("Id of the body and path do not match");
        }

        Project updatedProject = this.projectRepo.save(project);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Retrieves the information required for the add modal.
     *
     * @return ResponseEntity containing the add modal information in the response
     *         body.
     */
    @GetMapping(path = "/add", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAddModalInfo() throws ResourceNotFoundException {
        List<Map<String, Object>> teamsInfo = this.projectRepo.getTeamsInfo();
        List<Map<String, Object>> warehousesInfo = this.projectRepo.getWarehousesInfo();
        List<Map<String, Object>> productsInfo = this.projectRepo.getProductsInfo();

        Map<String, Object> response = new HashMap<>();
        response.put("teams", teamsInfo);
        response.put("warehouses", warehousesInfo);
        response.put("products", productsInfo);

        return ResponseEntity.ok(response);
    }
}
