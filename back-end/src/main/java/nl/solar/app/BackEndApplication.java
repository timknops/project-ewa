package nl.solar.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.transaction.Transactional;
import nl.solar.app.models.Project;
import nl.solar.app.repositories.EntityRepository;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        this.createSampleProjects();
    }

    @Autowired
    EntityRepository<Project> projectsRepo;

    private void createSampleProjects() {
        List<Project> projects = projectsRepo.findAll();

        if (!projects.isEmpty()) {
            return;
        }

        final int AMOUNT_OF_PROJECTS = 15;
        for (int i = 0; i < AMOUNT_OF_PROJECTS; i++) {
            Project project = Project.createDummyProject(0);
            projectsRepo.save(project);
        }
    }
}
