package nl.solar.app.rest;

import nl.solar.app.DTO.DashboardDTO;
import nl.solar.app.models.Email;
import nl.solar.app.models.Inventory;
import nl.solar.app.models.User;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.repositories.InventoryRepository;
import nl.solar.app.repositories.jpaRepositories.DashboardRepositoryJpa;
import nl.solar.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Controller class for handling requests related to dashboard items.
 * Manages operations such as retrieving dashboard data, forecasting stock shortages,
 * Notifying administrators about low stock.
 */
@RestController
@RequestMapping("/dashboard-items")
public class DashboardController {
    private final DashboardRepositoryJpa dashboardRepo;

    /**
     * Constructor for the DashboardController, injects the DashboardRepo
     *
     * @param dashboardRepo
     */
    @Autowired
    public DashboardController(DashboardRepositoryJpa dashboardRepo) {
        this.dashboardRepo = dashboardRepo;
    }

    /**
     * Retrieves a list of the dashboard items for the inventory of the dashboard table
     *
     * @return a list of DasboardDTO for the inventory dashboard table
     * @author Anonymized
     */
    @GetMapping("/inventory")
    public List<DashboardDTO> getDashboardItems() {
        return dashboardRepo.getDashboardItems();
    }

    /**
     * Retrieves a list of the dashboard items for the projects that are being used.
     *
     * @return a list of DasboardDTO of the project items
     * @author Anonymized
     */
    @GetMapping("/project")
    public List<DashboardDTO> getProjectDashboardItems() {
        return dashboardRepo.getProjectDashboardItems();
    }

    /**
     * Retrieves a list of the dashboard items for all the quantities of the current inventory
     *
     * @return a list of DasboardDTO of all the current inventories
     * @author Anonymized
     */
    @GetMapping("/inventory-quantity")
    public List<DashboardDTO> getInventoryQuantity() {
        return dashboardRepo.getInventoryQuantity();
    }

    @Autowired
    InventoryRepository inventoryRepo;

    @Autowired
    EntityRepository<User> userRepo;

    @Autowired
    private EmailService emailService;

    // quantity = the amount added by an order
    // inventoryQuantity = the current inventory level
    // amountOfProduct = the amount used by a project

    /**
     * Method that is run every once at start of application and then in 24 hour intervals
     * Gets the data used on the dashboard, and checks if at some point the amount of stock is below the minimum value
     * Sends an email to the admins on what product should be ordered to which warehouse
     */
    @Scheduled(initialDelay = 10000, fixedRate = 24 * 60 * 60 * 1000)
    public void forecastNotification() {
        List<DashboardDTO> originalQuantityList = getDashboardItems();
        List<DashboardDTO> originalAmountOfProductList = getProjectDashboardItems();

        // method to combine projects on the same date
        List<DashboardDTO> combinedQuantityList = combineQuantityList(originalQuantityList);

        // method to combine orders on the same date
        List<DashboardDTO> combinedAmountOfProductList = combineAmountOfProductList(originalAmountOfProductList);

        // method to add a order to the quantity list
        List<DashboardDTO> mergedList = mergeQuantityAndAmountOfProduct(combinedQuantityList, combinedAmountOfProductList);
        mergedList.sort(Comparator.comparing(DashboardDTO::getDeliverDate)); // list now sorted on date

        // method to extract the current stock levels
        Map<String, Integer> currentStockValues = extractCurrentStockValues(mergedList);

        // method to extract minimum value of product
        Map<String, Integer> minimumStockValues = extractMinimumValueProduct(inventoryRepo.findAll());

        // method to get any shortages
        Map<String, LocalDate> shortages = getStockShortages(currentStockValues, minimumStockValues, mergedList);

        // method to get users
        List<User> users = userRepo.findAll().stream()
                .filter(user -> "ADMIN".equals(user.getType()))
                .collect(Collectors.toList());

        if (!shortages.isEmpty() && !users.isEmpty()){
            // method to send email
            sendEmailNotification(shortages, users);
        }
    }

    /**
     * Sends an email to admin users
     * @param shortages Map with product for a specific warehouse and the date the stock would be insufficient
     * @param users List of users
     */
    private void sendEmailNotification(Map<String, LocalDate> shortages, List<User> users) {
        Email email = new Email();

        String body = formatShortagesString(shortages);
        email.setSubject("Stock shortage notification");
        email.setEmailBody(body);

        for (User user : users){
            email.setReceiver(user.getEmail()); // correct email
            emailService.sendMail(email);
        }
    }

    /**
     * Method that creates a single String with all the shortages in it
     * @param shortages Map with product for a specific warehouse and the date the stock would be insufficient
     * @return a String that shows all shortage
     */
    private String formatShortagesString(Map<String, LocalDate> shortages) {
        StringBuilder body = new StringBuilder();

        body.append("Hello,\n\n");
        body.append("This is an automated message letting you know that the following items need to be ordered, to " +
                "prevent them from having a shortage of stock for upcoming projects: \n\n");

        for (Map.Entry<String, LocalDate> entry : shortages.entrySet()) {
            String key = entry.getKey();
            LocalDate value = entry.getValue();

            body.append(key)
                    .append(" will have a shortage of stock on: ")
                    .append(value)
                    .append("\n");
        }

        body.append("\n\nPlease proceed to the order page to place the needed orders!");

        return body.toString();
    }

    /**
     * Method to combine the amount of a product to be added if the order is for the same day, product and warehouse
     * @param originalQuantityList List with all order data
     * @return List with combined order data
     */
    private static List<DashboardDTO> combineQuantityList(List<DashboardDTO> originalQuantityList) {
        Map<String, DashboardDTO> combinedMap = new HashMap<>();

        for (DashboardDTO dto : originalQuantityList) {
            String key = dto.getWarehouseName() + "_" + dto.getProductName() + "_" + dto.getDeliverDate();

            if (combinedMap.containsKey(key)) {
                DashboardDTO combinedDTO = combinedMap.get(key);
                combinedDTO.setQuantity(combinedDTO.getQuantity() + dto.getQuantity());
            } else {
                combinedMap.put(key, dto);
            }
        }

        return new ArrayList<>(combinedMap.values());
    }

    /**
     * Method to combine the amount of product to be used by a project if the project is for the same day, product and warehouse
     * @param originalAmountOfProductList List with all project data
     * @return List with combined project data
     */
    private static List<DashboardDTO> combineAmountOfProductList(List<DashboardDTO> originalAmountOfProductList) {
        Map<String, DashboardDTO> combinedMap = new HashMap<>();

        for (DashboardDTO dto : originalAmountOfProductList) {
            String key = dto.getWarehouseName() + "_" + dto.getProductName() + "_" + dto.getDueDate();

            if (combinedMap.containsKey(key)) {
                DashboardDTO combinedDTO = combinedMap.get(key);
                combinedDTO.setQuantity(combinedDTO.getAmountOfProduct() + dto.getAmountOfProduct());
            } else {
                combinedMap.put(key, dto);
            }
        }

        return new ArrayList<>(combinedMap.values());
    }

    /**
     * Method that merges the DTO if the date is the same
     * @param combinedQuantityList List with combined order data
     * @param combinedAmountOfProductList List with combined project data
     * @return List with the combined data of orders and projects for a specific date and warehouse
     */
    private static List<DashboardDTO> mergeQuantityAndAmountOfProduct(
            List<DashboardDTO> combinedQuantityList, List<DashboardDTO> combinedAmountOfProductList) {
        Map<String, DashboardDTO> mergedMap = new HashMap<>();

        for (DashboardDTO dto : combinedQuantityList) {
            String key = dto.getWarehouseName() + "_" + dto.getProductName() + "_" + dto.getDeliverDate();

            mergedMap.put(key, dto);
        }

        for (DashboardDTO dto : combinedAmountOfProductList) {
            String key = dto.getWarehouseName() + "_" + dto.getProductName() + "_" + dto.getDueDate();

            if (mergedMap.containsKey(key)) {
                DashboardDTO combinedDTO = mergedMap.get(key);
                combinedDTO.setAmountOfProduct(dto.getAmountOfProduct());
            } else {
                dto.setDeliverDate(dto.getDueDate());
                mergedMap.put(key, dto);
            }
        }

        return new ArrayList<>(mergedMap.values());
    }

    /**
     * Method to extract the current stock value of a specific product in a warehouse from the DTO
     * @param mergedList List with the combined data of orders and projects for a specific date and warehouse
     * @return Map containing the product and warehouse combination and the current stock
     */
    private static Map<String, Integer> extractCurrentStockValues(List<DashboardDTO> mergedList) {
        Map<String, Integer> currentStockValues = new HashMap<>();

        for (DashboardDTO dto : mergedList){
            String key = "Product: " + dto.getProductName() + ", in warehouse: " + dto.getWarehouseName();

            if (currentStockValues.containsKey(key)) {
                // continue
            } else {
                currentStockValues.put(key, dto.getInventoryQuantity());
            }
        }

        return currentStockValues;
    }

    /**
     * Method to extract the minimum values of a specific product in a warehouse from the DTO
     * @param inventories List of inventories
     * @return Map containing the product and warehouse combination and the minimum value
     */
    private static Map<String, Integer> extractMinimumValueProduct(List<Inventory> inventories) {
        Map<String, Integer> minimumStockValues = new HashMap<>();

        for (Inventory inventory : inventories){
            String key = "Product: " + inventory.getProduct().getProductName() +
                    ", in warehouse: " + inventory.getWarehouse().getName();

            if (minimumStockValues.containsKey(key)) {
                // continue
            } else {
                minimumStockValues.put(key, inventory.getMinimum());
            }
        }

        return minimumStockValues;
    }

    /**
     * Method that checks if at some point the products used and added would lead to a shortage of stock
     * @param currentStockValues Map containing the product and warehouse combination and the current stock
     * @param minimumStockValues Map containing the product and warehouse combination and the minimum value
     * @param mergedList List with the combined data of orders and projects for a specific date and warehouse
     * @return Map containing the product and warehouse combination and the date at which the amount of stock would
     * become insufficient
     */
    private static Map<String, LocalDate> getStockShortages(Map<String, Integer> currentStockValues, Map<String, Integer> minimumStockValues,
                                                            List<DashboardDTO> mergedList) {
        Map<String, LocalDate> shortagesMap = new HashMap<>();

        for (DashboardDTO dto : mergedList){
            String key = "Product: " + dto.getProductName() + ", in warehouse: " + dto.getWarehouseName();
            currentStockValues.put(key, (currentStockValues.get(key) + dto.getQuantity()) - dto.getAmountOfProduct());
            if (currentStockValues.get(key) < minimumStockValues.get(key)){
                if (shortagesMap.containsKey(key)){
                    // continue
                } else {
                    shortagesMap.put(key, dto.getDeliverDate());
                }
            }
        }

        return shortagesMap;
    }
}
