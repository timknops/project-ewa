package nl.solar.app.repositories;

import nl.solar.app.DTO.DashboardDTO;
import nl.solar.app.repositories.jpaRepositories.DashboardRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit test class for testing the methods of DashboardRepositoryJpa
 *
 * @author Hanan Ouardi
 */
@EntityScan(basePackages = "nl.solar.app.models")
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = { Repository.class }))

public class DashboardRepositoryJPATest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DashboardRepositoryJpa dashboardRepository;

    /**
     * Tests the getDasboardItems() method
     * Verifies that the list is not empty
     */
    @Test
    public void testGetDashboardItems() {
        List<DashboardDTO> dashboardItems = dashboardRepository.getDashboardItems();
        assertThat(dashboardItems).isNotEmpty();
    }
    /**
     * Tests the getprojectDashboardItems() method
     * Verifies that the list is not empty
     */
    @Test
    public void testGetProjectDashboardItems() {
        List<DashboardDTO> projectDashboardItems = dashboardRepository.getProjectDashboardItems();
        assertThat(projectDashboardItems).isNotEmpty();
    }
    /**
     * Tests the getInventoryQuanity() method
     */
    @Test
    public void testGetInventoryQuantity() {
        List<DashboardDTO> inventoryQuantities = dashboardRepository.getInventoryQuantity();
        assertThat(inventoryQuantities).isNotEmpty();
        assertThat(inventoryQuantities).allMatch(item ->
                item.getWarehouseId() != null &&
                        item.getWarehouseName() != null &&
                        item.getProductName() != null &&
                        item.getInventoryQuantity() >= 0 &&
                        item.getProductId() != null
        );
    }

}
