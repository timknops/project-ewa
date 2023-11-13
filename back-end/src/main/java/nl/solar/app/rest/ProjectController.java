package nl.solar.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.solar.app.models.Project;
import nl.solar.app.repositories.ProjectRepository;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepo;

    @GetMapping(produces = "application/json")
    public List<Project> getAll() {
        return this.projectRepo.findAll();
    }
}
