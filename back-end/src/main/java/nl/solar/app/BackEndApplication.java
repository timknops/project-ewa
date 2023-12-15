package nl.solar.app;

import jakarta.transaction.Transactional;
import nl.solar.app.models.*;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import nl.solar.app.repositories.ItemRepository;
import nl.solar.app.repositories.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {

    // All repositories.
    @Autowired
    EntityRepository<Warehouse> warehouseRepo;

    @Autowired
    EntityRepository<Team> teamsRepo;

    @Autowired
    EntityRepository<Order> orderRepo;

    @Autowired
    EntityRepository<Project> projectsRepo;

    @Autowired
    ResourceRepository resourcesRepo;

    @Autowired
    EntityRepository<Product> productsRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    @Autowired
    ItemRepository itemRepo;

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        this.createSampleWarehouse();
        this.createSampleTeams();
        this.createSampleOrders();
        this.createSampleProjects();
        this.createSampleProducts();
        this.createSampleResources();
        this.createDummyInventory();
        this.createDummyItems();
    }

    /**
     * Create sample data for warehouse
     *
     * @author Julian Kruithof
     */
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
            Warehouse warehouse = Warehouse.createDummyWarehouse(names[i], locations[i]);
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

        List<Warehouse> warehouses = warehouseRepo.findAll();

        final int AMOUNT_OF_TEAMS = 6;
        for (int i = 0; i < AMOUNT_OF_TEAMS; i++) {
            Warehouse randomWarehouse = warehouses.get((int) (Math.random() * warehouses.size()));
            Team team = Team.createDummyTeam();

            // Set the warehouse for the team.
            team.setWarehouse(randomWarehouse);

            teamsRepo.save(team);
        }
    }

    /**
     * Creates sample orders
     *
     * @author Julian Kruithof
     */
    private void createSampleOrders() {
        List<Order> orders = this.orderRepo.findAll();

        if (!orders.isEmpty())
            return;

        List<Warehouse> warehouses = this.warehouseRepo.findAll();
        for (Warehouse warehouse : warehouses) {
            Random random = new Random();
            int randomInt = random.nextInt(5) + 1;

            for (int i = 0; i < randomInt; i++) {
                Order order = Order.createDummyOrder(warehouse);

                this.orderRepo.save(order);

                // bidirectional
                warehouse.getOrders().add(order);
            }
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

        final String[] PROJECT_DESCRIPTIONS = {
                "Solar Sedum is proud to announce the successful completion of Project GreenSky, where we transformed urban rooftops into vibrant, eco-friendly spaces.",
                "Superzon is a project that aims to provide solar energy to the entire city of Amsterdam. We are proud to announce that we have successfully completed the first phase of this project.",
                "The Switch is a project that aims to provide solar energy to the entire city of Amsterdam. We are proud to announce that we have successfully completed the second phase of this project.",
                "Induct is a project that aims to provide solar energy to the entire city of Amsterdam. We are proud to announce that we have successfully completed the first phase of this project.",
        };

        final int AMOUNT_OF_PROJECTS = 15;
        for (int i = 0; i < AMOUNT_OF_PROJECTS; i++) {
            Project project = Project
                    .createDummyProject(PROJECT_DESCRIPTIONS[(int) (Math.random() * PROJECT_DESCRIPTIONS.length)]);

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

                if (product.getResources().contains(resource) || project.getResources().contains(resource))
                    continue;

                // Ensure bidirectional relationship.
                project.getResources().add(resource);
                product.getResources().add(resource);

                productsRepo.save(product);
                projectsRepo.save(project);
            }
        }
    }

    /**
     * Add dummy inventories to the database
     *
     * @author Julian Kruithof
     */
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

    private void createDummyItems() {
        List<Item> items = itemRepo.findAll();

        if (!items.isEmpty())
            return;
        List<Product> products = this.productsRepo.findAll();
        List<Order> orders = this.orderRepo.findAll();
        Random random = new Random();

        // ensure every order has at least one item
        for (Order order : orders) {
            Item item = new Item();
            item.setOrder(order);
            Product product = products.get(random.nextInt(products.size()));
            item.setProduct(product);
            item.setQuantity(random.nextLong(50L) + 1);

            order.getItems().add(item);
            product.getItems().add(item);
        }

        // add 10 items to random orders
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            Order order = orders.get(random.nextInt(orders.size()));
            item.setOrder(order);
            Product product = products.get(random.nextInt(products.size()));
            item.setProduct(product);
            item.setQuantity(random.nextLong(50L));

            order.getItems().add(item);
            product.getItems().add(item);
        }
    }
}
