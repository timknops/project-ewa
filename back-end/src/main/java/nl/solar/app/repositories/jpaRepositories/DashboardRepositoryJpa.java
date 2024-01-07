package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.DTO.DashboardDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
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


    public List<DashboardDTO> getProjectDashboardItems() {
        List<Object[]> projectResults = entityManager.createQuery(
                        "SELECT CAST(p.dueDate AS DATE) AS dueDate, " +
                                "r.project.id AS projectId, p.projectName, " +
                                "w.id AS warehouseId, w.name AS warehouseName, " +
                                "r.product.id AS productId, pr.productName, " +
                                "r.quantity AS amountOfProduct " +
                                "FROM Project p " +
                                "JOIN Resource r ON p.id = r.project.id " +
                                "JOIN Team t ON p.team.id = t.id " +
                                "JOIN Warehouse w ON t.warehouse.id = w.id " +
                                "JOIN Product pr ON r.product.id = pr.id " +
                                "GROUP BY CAST(p.dueDate AS DATE), r.project.id, p.projectName, " +
                                "w.id, w.name, r.product.id, pr.productName, r.quantity " +
                                "ORDER BY CAST(p.dueDate AS DATE)"
                        , Object[].class)
                .getResultList();

        return projectResults.stream()
                .map(result -> new DashboardDTO(
                        ((java.sql.Date) result[0]).toLocalDate(), // dueDate
                        (Long) result[1], // projectId
                        (String) result[2], // projectName
                        (Long) result[3], // warehouseId
                        (String) result[4], // warehouseName
                        (Long) result[5], // productId
                        (String) result[6], // productName
                        ((Number) result[7]).intValue() // amountOfProduct
                ))
                .collect(Collectors.toList());
    }

    public List<DashboardDTO> getInventoryQuantity() {
        List<Object[]> inventoryResults = entityManager.createQuery(
                        "SELECT " +
                                "w.id AS warehouseId, w.name AS warehouseName, " +
                                "p.id AS productId, p.productName AS productName, " +
                                "COALESCE(SUM(inv.quantity), 0) AS inventoryQuantity " +
                                "FROM Warehouse w " +
                                "JOIN Product p ON 1 = 1 " +
                                "LEFT JOIN Inventory inv ON p.id = inv.product.id AND w.id = inv.warehouse.id " +
                                "GROUP BY w.id, w.name, p.id, p.productName"
                        , Object[].class)
                .getResultList();

        return inventoryResults.stream()
                .map(result -> new DashboardDTO(
                        (Long) result[0], // warehouseId
                        (String) result[1], // warehouseName
                        (String) result[3], // productName
                        ((Number) result[4]).intValue(), // inventoryQuantity
                        (Long) result[2] // productId

                ))
                .collect(Collectors.toList());
    }

}
