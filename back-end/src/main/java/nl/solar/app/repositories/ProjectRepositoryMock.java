package nl.solar.app.repositories;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import nl.solar.app.models.Project;
import nl.solar.app.models.Team;
import nl.solar.app.models.Warehouse;
import nl.solar.app.models.Product;

/**
 * This class is a mock repository for the Project model.
 * 
 * @author Tim Knops
 */
@Repository("PROJECTS.INMEMORY")
public class ProjectRepositoryMock implements ProjectRepository {

    private List<Project> projects;
    private List<Team> teams;
    private Warehouse warehouse;
    private List<Product> products;
    private long currentId = 0;

    private final EntityRepository<Team> teamRepo;
    private final EntityRepository<Warehouse> warehouseRepo;
    private final EntityRepository<Product> productRepo;

    public ProjectRepositoryMock(EntityRepository<Team> teamRepo, EntityRepository<Warehouse> warehouseRepo,
            EntityRepository<Product> productRepo) {
        this.teamRepo = teamRepo;
        this.warehouseRepo = warehouseRepo;
        this.productRepo = productRepo;
        this.projects = new ArrayList<>();
        this.teams = this.teamRepo.findAll();
        this.warehouse = this.warehouseRepo.findAll().get(0);
        this.products = this.productRepo.findAll();

        int amountOfProjects = 15;

        for (int i = 0; i < amountOfProjects; i++) {
            Team randomTeam = teams.get((int) (Math.random() * teams.size()));

            this.projects.add(Project.createDummyProject(++currentId, "Project " + (i + 1), randomTeam,
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

    @Override
    public Object getAddModalInfo() {

        return null;
    }

}
