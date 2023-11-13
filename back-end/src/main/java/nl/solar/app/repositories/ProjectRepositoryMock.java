package nl.solar.app.repositories;

import java.util.List;
import org.springframework.stereotype.Repository;
import nl.solar.app.models.Project;
import nl.solar.app.models.Team;

@Repository("PROJECTS.INMEMORY")
public class ProjectRepositoryMock implements ProjectRepository {

    private List<Project> projects;
    private long currentId = 0;

    private final EntityRepository<Team> teamRepo;

    public ProjectRepositoryMock(EntityRepository<Team> teamRepo) {
        this.teamRepo = teamRepo;
        List<Team> teams = this.teamRepo.findAll();

        int amountOfProjects = 15;

        for (int i = 0; i < amountOfProjects; i++) {
            Team randomTeam = teams.get((int) (Math.random() * teams.size()));

            projects.add(Project.createDummyProject(++currentId, "Project " + (i + 1), randomTeam,
                    "Client " + (i + 1)));
        }
    }

    @Override
    public List<Project> findAll() {
        return projects;
    }

    @Override
    public Project findById(long id) {
        // Find the project with the given id, if it doesn't exist return null.
        return projects.stream().filter(project -> project.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Project delete(long id) {
        Project toBeDeleted = this.findById(id);

        if (toBeDeleted != null) {
            projects.remove(toBeDeleted);
        }

        return toBeDeleted;
    }

    @Override
    public Project save(Project item) {
        int index = projects.indexOf(item);

        if (index != -1) {
            projects.set(index, item);
        } else {
            if (item.getId() == 0) { // If the id is not set, set it to the next available id.
                item.setId(++currentId);
            }

            projects.add(item);
        }

        return item;
    }
}
