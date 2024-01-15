package nl.solar.app.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.solar.app.DTO.InventoryProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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

import nl.solar.app.DTO.ProjectResourceDTO;
import nl.solar.app.enums.ProjectStatus;
import nl.solar.app.exceptions.PreConditionFailedException;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.Project;
import nl.solar.app.models.Resource;
import nl.solar.app.models.Team;
import nl.solar.app.models.Warehouse;
import nl.solar.app.models.views.ProjectView;
import nl.solar.app.models.wrappers.ProjectRequestWrapper;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
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

    @Autowired
    InventoryRepository inventoryRepo;

    /**
     * Retrieves a list of all projects specifically for the overview of the
     * projects.
     * 
     * @return A list of all projects.
     */
    @JsonView(ProjectView.Overview.class)
    @GetMapping(produces = "application/json")
    public List<Project> getAll() {
        List<Project> projects = this.projectRepo.findAll();
        return projects;
    }

    /**
     * Retrieves a project by its id.
     *
     * @param id the id of the project to retrieve.
     * @return A ResponseEntity containing the retrieved project.
     * @throws ResourceNotFoundException If the project with the given ID does not
     *                                   exist.
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getProject(@PathVariable Long id) {
        Project project = this.projectRepo.findById(id);

        // If the project doesn't exist, return a value so that the frontend can handle
        // it.
        if (project == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Project with id: " + id + " was not found");
            return ResponseEntity.ok(response);
        }

        // Format the project to a map.
        Map<String, Object> formattedProject = new HashMap<>();
        formattedProject.put("id", project.getId());
        formattedProject.put("projectName", project.getProjectName());
        formattedProject.put("client", project.getClient());
        formattedProject.put("dueDate", project.getDueDate());
        formattedProject.put("status", project.getStatus());
        formattedProject.put("teamId", project.getTeam().getId());
        formattedProject.put("teamName", project.getTeam().getTeam());
        formattedProject.put("description", project.getDescription());

        // Get the resources for the project.
        List<ProjectResourceDTO> resources = this.resourceRepo.getProjectResources(id);

        // Add the resources to the formatted project.
        formattedProject.put("resources", resources);

        return ResponseEntity.ok(formattedProject);
    }

    /**
     * Retrieves a list of all projects for a team.
     *
     * @param teamId the id of the team to retrieve projects for.
     * @return A ResponseEntity containing the retrieved projects.
     * @throws ResourceNotFoundException If the team with the given ID does not
     *                                   exist.
     */
    @GetMapping(path = "/team/{teamId}", produces = "application/json")
    public ResponseEntity<List<Project>> getProjectsForTeam(@PathVariable Long teamId)
            throws ResourceNotFoundException {
        // Check if the team exists.
        Team team = this.teamRepo.findById(teamId);
        if (team == null) {
            throw new ResourceNotFoundException("Team with id: " + teamId + " was not found");
        }

        // Get the projects for the team.
        List<Project> projects = this.projectRepo.findByTeamId(teamId);

        return ResponseEntity.ok(projects);
    }

    /**
     * Deletes a project by its id.
     *
     * @param id the id of the project to delete.
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
     * @param projectWrapper a wrapper containing the project and the resources.
     * @return A ResponseEntity containing the created project.
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequestWrapper projectWrapper)
            throws ResourceNotFoundException {
        // Check if the team exists.
        if (projectWrapper.getProject().getTeam() == null) {
            throw new ResourceNotFoundException(
                    "Team with id: " + projectWrapper.getProject().getTeam().getId() + " was not found");
        }

        // Check if the team id is valid.
        Team team = this.teamRepo.findById(projectWrapper.getProject().getTeam().getId());
        if (team == null) {
            throw new ResourceNotFoundException(
                    "Team with id: " + projectWrapper.getProject().getTeam().getId() + " was not found");
        }

        // Save the project.
        Project createdProject = this.projectRepo.save(projectWrapper.getProject());

        // Add the resources to the project if there are any.
        List<ProjectResourceDTO> resources = projectWrapper.getResources();
        if (resources != null && !resources.isEmpty()) {
            this.resourceRepo.addProjectResources(createdProject.getId(), resources);
        }

        // Create the URI for the created project.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProject.getId()).toUri();

        return ResponseEntity.created(location).body(createdProject);
    }

    /**
     * Updates a project by its id.
     *
     * @param id             the id of the project to update.
     * @param projectWrapper a wrapper containing the project and the resources.
     * @return A ResponseEntity containing the updated project.
     * @throws PreConditionFailedException If the project with the given ID does not
     *                                     exist.
     * @throws PreConditionFailedException If the id of the body and path do not
     *                                     match.
     */
    @PutMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
            @RequestBody ProjectRequestWrapper projectWrapper)
            throws PreConditionFailedException, ResourceNotFoundException {
        if (id != projectWrapper.getProject().getId()) {
            throw new PreConditionFailedException("Id of the body and path do not match");
        }

        // Check if the project exists.
        Project project = this.projectRepo.findById(id);
        if (project == null) {
            throw new ResourceNotFoundException("Project with id: " + id + " was not found");
        }

        // Save the old status of the project.
        ProjectStatus oldStatus = project.getStatus();

        // Update the project.
        Project updatedProject = this.projectRepo.save(projectWrapper.getProject());

        // Delete all existing resources.
        this.resourceRepo.deleteProjectResources(id);

        // Add back the resources that are still in the projectWrapper.
        for (ProjectResourceDTO resource : projectWrapper.getResources()) {
            this.resourceRepo.save(new Resource(updatedProject, resource.getProduct(), resource.getQuantity()));
        }

        // Check if the project went from upcoming to in progress. If so, update the
        // inventory.
        if (oldStatus == ProjectStatus.UPCOMING && updatedProject.getStatus() == ProjectStatus.IN_PROGRESS) {
            // Get the team of the project.
            Team team = this.teamRepo.findById(updatedProject.getTeam().getId());

            if (team == null) {
                throw new ResourceNotFoundException(
                        "Team with id: " + updatedProject.getTeam().getId() + " was not found");
            }

            // Get the warehouse of the team.
            Warehouse warehouse = team.getWarehouse();

            if (warehouse == null) {
                throw new ResourceNotFoundException(
                        "Warehouse with id: " + team.getWarehouse().getId() + " was not found");
            }

            // Update the inventory asynchronously.
            updateInventory(updatedProject, warehouse);
        }

        // Create the URI for the updated project.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(updatedProject.getId()).toUri();

        return ResponseEntity.created(location).body(updatedProject);
    }

    /**
     * Asynchronously updates the inventory of a warehouse based on the resources.
     * 
     * @param updatedProject the updated project
     * @param warehouse      the warehouse of the team
     */
    @Async
    public void updateInventory(Project updatedProject, Warehouse warehouse) {
        // Get all the resources of the project and the inventories of the warehouse.
        List<ProjectResourceDTO> resources = this.resourceRepo.getProjectResources(updatedProject.getId());
        List<InventoryProductDTO> inventories = this.inventoryRepo.findInventoryForWarehouse(warehouse.getId());

        // Map for efficient lookup of inventories based on product ID.
        Map<Long, Inventory> inventoryMap = new HashMap<>();
        for (Inventory inventory : warehouse.getInventory()) {
            inventoryMap.put(inventory.getProduct().getId(), inventory);
        }

        // Update the inventories based on resources.
        for (ProjectResourceDTO resource : resources) {
            Long productId = resource.getProduct().getId();
            Inventory inventory = inventoryMap.get(productId);

            if (inventory != null) {
                long newQuantity = inventory.getQuantity() - resource.getQuantity();
                inventory.setQuantity(newQuantity);
                this.inventoryRepo.save(inventory);
            }
        }
    }

    /**
     * Retrieves the information required for the add/update modal.
     *
     * @return ResponseEntity containing the add modal information in the response
     *         body.
     */
    @GetMapping(path = "/modal", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAddModalInfo() throws ResourceNotFoundException {
        List<Map<String, Object>> teamsInfo = this.projectRepo.getTeamsInfo();
        List<Map<String, Object>> productsInfo = this.projectRepo.getProductsInfo();

        Map<String, Object> response = new HashMap<>();
        response.put("teams", teamsInfo);
        response.put("products", productsInfo);

        return ResponseEntity.ok(response);
    }
}
