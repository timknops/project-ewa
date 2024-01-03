package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.DTO.DashboardDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
@Transactional
public class DashboardRepositoryJpa  {

    @PersistenceContext
    private EntityManager entityManager;
    public List<DashboardDTO> getDashboardItems() {
            List<Object[]> results = entityManager.createQuery(
                            "SELECT  w.id AS warehouseId, w.name AS warehouseName, p.productName AS productName, i.quantity AS itemQuantity, inv.quantity AS inventoryQuantity, " +
                                    "CAST(o.deliverDate AS DATE) AS deliverDate " +
                                    "FROM Item  i " +
                                    "JOIN Order o ON i.order.id = o.id " +
                                    "JOIN Product p ON i.product.id = p.id " +
                                    "JOIN Warehouse w ON o.warehouse.id = w.id " +
                                    "LEFT JOIN Inventory inv ON p.id = inv.product.id AND w.id = inv.warehouse.id"
                            , Object[].class)
                    .getResultList();


            return results.stream()
                    .map(result -> new DashboardDTO(
                            (Long) result[0], //warehouseId
                            (String) result[1], // warehouseName
                            (String) result[2], // productName
                            ((Number) result[3]).intValue(),// quantity
                            ((Number) result[4]).intValue(), //inventoryQuantity
                            ((java.sql.Date) result[5]).toLocalDate() // deliverDate
                    ))
                    .collect(Collectors.toList());

    }

    @Scheduled(fixedRate = 20000) // fixedRate is low for testing (fixedRate = 24, timeUnit = TimeUnit.HOURS) for daily
    protected void forecastNotification (){
        System.out.println("Scheduled method called:\n");

        List<DashboardDTO> originalList = getDashboardItems();

        System.out.println("The original:");
        System.out.println(originalList);

        System.out.println("\n\n The combined:\n");

        List<DashboardDTO> combinedList = combineQuantities(originalList);

        // Print the combined list
        for (DashboardDTO dto : combinedList) {
            System.out.print(dto);
        }
    }

    private static List<DashboardDTO> combineQuantities(List<DashboardDTO> originalList) {
        Map<String, DashboardDTO> combinedMap = new HashMap<>();

        for (DashboardDTO dto : originalList) {
            String key = dto.getWarehouseId() + "_" + dto.getProductName();

            if (combinedMap.containsKey(key)) {
                DashboardDTO combinedDTO = combinedMap.get(key);
                combinedDTO.setQuantity(combinedDTO.getQuantity() + dto.getQuantity());
                // You can also update other fields if needed
            } else {
                combinedMap.put(key, new DashboardDTO(dto));
            }
        }

        return new ArrayList<>(combinedMap.values());
    }
}
