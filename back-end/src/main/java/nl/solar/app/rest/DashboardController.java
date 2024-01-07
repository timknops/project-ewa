package nl.solar.app.rest;

import jakarta.annotation.PostConstruct;
import nl.solar.app.DTO.DashboardDTO;
import nl.solar.app.DTO.InventoryDTO;
import nl.solar.app.models.Email;
import nl.solar.app.models.Inventory;
import nl.solar.app.repositories.InventoryRepository;
import nl.solar.app.repositories.jpaRepositories.DashboardRepositoryJpa;
import nl.solar.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/dashboard-items")
public class DashboardController {

    private final DashboardRepositoryJpa dashboardRepo;

    @Autowired
    public DashboardController(DashboardRepositoryJpa dashboardRepo) {
        this.dashboardRepo = dashboardRepo;
    }

    @GetMapping("/inventory")
    public List<DashboardDTO> getDashboardItems() {
        return dashboardRepo.getDashboardItems();
    }

    @GetMapping("/project")
    public List<DashboardDTO> getProjectDashboardItems() {
        return dashboardRepo.getProjectDashboardItems();
    }

    @GetMapping("/inventory-quantity")
    public List<DashboardDTO> getInventoryQuantity() {
        return dashboardRepo.getInventoryQuantity();
    }

    @Autowired
    InventoryRepository inventoryRepo;

    @Autowired
    private EmailService emailService;

    // quantity = the amount added by an order
    // inventoryQuantity = the current inventory level
    // amountOfProduct = the amount used by a project
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

        if (!shortages.isEmpty()){
            // method to send email
            sendEmailNotification(shortages);
        }
    }

    // method to send an email
    private void sendEmailNotification(Map<String, LocalDate> shortages) {
        Email email = new Email();

        String body = formatShortagesString(shortages);

        email.setReceiver("wilcovdpol1999@gmail.com"); // correct email
        email.setEmailBody(body);
        email.setSubject("Stock shortage notification");

        emailService.sendMail(email);
    }

    // method to format the Map into a String
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

        return body.toString();
    }

    // method to combine the quantities of a certain product for orders on the same day
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

    // method to combine the quantities of a certain product for projects on the same day
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

    // method to combine the list of orders and the list of project where the date is the same
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

    // method to extract current stock value
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

    // method to extract minimum value of a products stock
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

    // method to get the date at which a stock value wouldn't be enough anymore
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
