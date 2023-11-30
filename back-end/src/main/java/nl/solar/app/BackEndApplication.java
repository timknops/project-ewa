package nl.solar.app;

import java.util.List;

import nl.solar.app.models.*;
import nl.solar.app.repositories.InventoryRepository;
import nl.solar.app.repositories.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.transaction.Transactional;
import nl.solar.app.repositories.EntityRepository;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        this.createSampleWarehouse();
        this.createSampleTeams();
        this.createSampleProjects();
        this.createSampleProducts();
        this.createSampleResources();
        this.createDummyInventory();
    }

    // All repositories.
    @Autowired
    EntityRepository<Warehouse> warehouseRepo;

    @Autowired
    EntityRepository<Team> teamsRepo;

    @Autowired
    EntityRepository<Project> projectsRepo;

    @Autowired
    ResourceRepository resourcesRepo;

    @Autowired
    EntityRepository<Product> productsRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    private void createSampleWarehouse() {
        List<Warehouse> warehouses = warehouseRepo.findAll();

        if (!warehouses.isEmpty())
            return;

        final String[] names = {
                "Solar Sedum",
                "Superzon",
                "The switch",
                "Induct",
                "EHES"
        };

        final String[] locations = {
                "H.J.E. Wenckebachweg 47D, 1096AK Amsterdam",
                "Marconistraat 4A, 1704RG Heerhugowaard",
                "Barndegat 8, 1505HN Zaandam",
                "Philippusweg 2, 3125AS Schiedam",
                "Bolwerk 5, 3905NH Veenendaal"
        };

        for (int i = 0; i < names.length; i++) {
            Warehouse warehouse = new Warehouse(0, names[i], locations[i]);
            warehouseRepo.save(warehouse);
        }
    }

    /**
     * Creates sample teams.
     * 
     * @author Tim Knops
     */
    private void createSampleTeams() {
        List<Team> teams = teamsRepo.findAll();

        if (!teams.isEmpty()) {
            return;
        }

        final int AMOUNT_OF_TEAMS = 6;
        for (int i = 0; i < AMOUNT_OF_TEAMS; i++) {
            Team team = Team.createDummyTeam();
            teamsRepo.save(team);
        }
    }

    /**
     * Creates sample projects and assigns them to a random team.
     * 
     * @throws RuntimeException if there are no teams.
     * @author Tim Knops
     */
    private void createSampleProjects() throws RuntimeException {
        List<Project> projects = projectsRepo.findAll();

        if (!projects.isEmpty()) {
            return;
        }

        List<Team> teams = teamsRepo.findAll();

        // Throw an exception if there are no teams.
        if (teams.isEmpty()) {
            throw new RuntimeException("No teams found.");
        }

        final int AMOUNT_OF_PROJECTS = 15;
        for (int i = 0; i < AMOUNT_OF_PROJECTS; i++) {
            Project project = Project.createDummyProject();

            // Give each project a random team.
            Team team = teams.get((int) (Math.random() * teams.size()));
            project.setTeam(team); // Set the team for the project.
            team.getProjects().add(project); // Add the project to the team.

            projectsRepo.save(project);
        }
    }

    /**
     * Creates sample products.
     * 
     * @author Tim Knops
     */
    private void createSampleProducts() {
        List<Product> products = productsRepo.findAll();

        if (!products.isEmpty()) {
            return;
        }

        final String[] PRODUCT_NAMES = {
                "Enphase IQ8+ omvormer",
                "Enphase Q kabel 1 fase",
                "Gateway / envoy",
                "Enphase IQ8M  omvormer",
                "Enphase Q Relay 1 fase",
                "Enphase Q Relay 3 fase",
                "MB glas/glas 380"
        };

        for (String productName : PRODUCT_NAMES) {
            Product product = Product.createDummyProducts(0, productName, "Dummy description");
            productsRepo.save(product);
        }
    }

    /**
     * Creates sample resources.
     * 
     * @author Tim Knops
     */
    private void createSampleResources() {
        List<Resource> resources = resourcesRepo.findAll();

        if (!resources.isEmpty()) {
            return;
        }

        // Get all projects and products.
        List<Project> projects = projectsRepo.findAll();
        List<Product> products = productsRepo.findAll();

        // Give each project a random amount of products between 1 and 5.
        for (Project project : projects) {
            int amountOfProducts = (int) (Math.random() * 5) + 1;

            for (int i = 0; i < amountOfProducts; i++) {
                Product product = products.get((int) (Math.random() * products.size()));
                Resource resource = new Resource(project, product, (int) (Math.random() * 50) + 1);

                if (product.getProjects().contains(resource) || project.getProducts().contains(resource))
                    continue;

                // Ensure bidirectional relationship.
                project.getProducts().add(resource);
                product.getProjects().add(resource);

                productsRepo.save(product);
                projectsRepo.save(project);
            }
        }
    }

    private void createDummyInventory() {
        List<Inventory> inventoryList = inventoryRepo.findAll();

        if (!inventoryList.isEmpty())
            return;

        for (Product product : productsRepo.findAll()) {
            for (Warehouse warehouse : warehouseRepo.findAll()) {
                Inventory inventory = Inventory.createDummyResource(warehouse, product);

                product.getInventory().add(inventory);
                warehouse.getInventory().add(inventory);

                productsRepo.save(product);
                warehouseRepo.save(warehouse);
            }
        }
    }

}
