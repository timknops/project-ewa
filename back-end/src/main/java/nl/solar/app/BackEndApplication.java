package nl.solar.app;

import jakarta.transaction.Transactional;
import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.*;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import nl.solar.app.repositories.ItemRepository;
import nl.solar.app.repositories.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class BackEndApplication implements CommandLineRunner {

    // All repositories.
    @Autowired
    EntityRepository<User> userRepo;

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

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Only create sample data if the database is empty.
        if (!this.ddlAuto.matches("create-drop|create")) {
            return;
        }

        this.createSampleWarehouse();
        this.createSampleTeams();
        this.createSampleOrders();
        this.createSampleUser();
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

        final String[] names = {
                "Team 1",
                "Team 2",
                "Team 3",
                "Team 4",
                "Team 5",
        };

        List<Warehouse> warehouses = warehouseRepo.findAll();

        for (Warehouse warehouse : warehouses) {
            Team.TeamType teamType = (warehouse.getId() == 1000) ? Team.TeamType.INTERNAL : Team.TeamType.EXTERNAL;

            String teamName = names[(int) (warehouse.getId() % names.length)];
            Team team = Team.createDummyTeam(warehouse, teamName, teamType);

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
     * Create sample data for user
     *
     * @author Noa de Greef
     */
    private void createSampleUser() throws ResourceNotFoundException {
        List<User> users = userRepo.findAll();
        List<Team> teams = teamsRepo.findAll();

        if (teams == null) {
            throw new ResourceNotFoundException("No teams were found");
        }

        teams.get(0).setTeam("Static Users");
        for (User staticUser : User.createStaticAdmin(teams.get(0))) {
            userRepo.save(staticUser);
        }

        userRepo.save(User.createStaticUser(teams.get(0)));

        if (!users.isEmpty())
            return;
        for (int i = 0; i < 11; i++) {
            // get a random team, except for the first team since that one is reserved for
            // static user
            Team team = teams.get((int) Math.floor(Math.random() * (teams.size() - 1)) + 1);
            User user = User.creatyDummyUser(i, team);
            team.getUsers().add(user);

            userRepo.save(user);
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

        final String[] PRODUCT_DESCRIPTONS = {
                "Inverters are devices that convert direct current (DC) generated by solar panels into alternating current (AC) for use in homes or the electrical grid.",
                "Cable designed for single-phase electrical systems. Cables like these are crucial for connecting solar panels and inverters.",
                "The Gateway or Envoy is a communication device used in solar energy systems. It may facilitate communication between different components of the solar setup.",
                "Similar to the iq8+ inverter, only with a higher maximum input power and a higher maximum Apparent power.",
                "A relay designed for use in single-phase systems. Relays are switches that control the flow of electricity.",
                "Similar to the Enphase Q Relay 1 fase, but designed for three-phase electrical systems.",
                "Solar panel created with two glass layers for extra protection. It can produces op to 380W in energy"
        };

        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
            Product product = Product.createDummyProducts(0, PRODUCT_NAMES[i], PRODUCT_DESCRIPTONS[i]);
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
                Inventory inventory = Inventory.createDummyInventory(warehouse, product);

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
            order.setTag(order.getTag().replaceFirst("\\.\\.\\.", product.getProductName()));

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
