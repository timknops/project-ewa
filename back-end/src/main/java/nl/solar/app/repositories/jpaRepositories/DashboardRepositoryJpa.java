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
                            "SELECT  w.id AS warehouseId, w.name AS warehouseName, p.productName AS productName, i.quantity AS quantity, " +
                                    "CAST(o.deliverDate AS DATE) AS deliverDate " +
                                    "FROM Item  i " +
                                    "JOIN Order o ON i.order.id = o.id " +
                                    "JOIN Product p ON i.product.id = p.id " +
                                    "JOIN Warehouse w ON o.warehouse.id = w.id"
                            , Object[].class)
                    .getResultList();


            return results.stream()
                    .map(result -> new DashboardDTO(
                            (Long) result[0], //warehouseId
                            (String) result[1], // warehouseName
                            (String) result[2], // productName
                            ((Number) result[3]).intValue(), // quantity
                            ((java.sql.Date) result[4]).toLocalDate() // deliverDate
                    ))
                    .collect(Collectors.toList());

        }

}
