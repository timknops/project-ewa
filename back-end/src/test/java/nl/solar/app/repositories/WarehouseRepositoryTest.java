package nl.solar.app.repositories;

import jakarta.persistence.Entity;
import nl.solar.app.models.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the WarehouseRepositoryJpa
 * @author Wilco van de Pol
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = { Repository.class, Entity.class }), showSql = false)
public class WarehouseRepositoryTest {

    @Autowired
    CommandLineRunner commandLineRunner;

    @Qualifier("WAREHOUSE.JPA")
    @Autowired
    private EntityRepository<Warehouse> warehouseRepository;

    private List<Warehouse> warehouseList;

    @BeforeEach
    public void setup() throws Exception {
        commandLineRunner.run(null);
        warehouseList = warehouseRepository.findAll();
    }

    @Test
    public void findAllReturnsWarehouses() {
        assertFalse(warehouseList.isEmpty(), "List of warehouses should not be empty");
    }

    @Test
    public void findByIdReturnsSpecificWarehouse() {
        assertFalse(warehouseList.isEmpty(), "List of warehouses should not be empty");

        // check for all warehouses if it can be found using its id
        for (Warehouse warehouse : warehouseList){
            Warehouse specificWarehouse = warehouseRepository.findById(warehouse.getId());
            assertEquals(warehouse, specificWarehouse, "Warehouse should be the same as the result of the find by id");
        }

        // check non-existing warehouse
        Warehouse warehouse = warehouseRepository.findById(999999);
        assertNull(warehouse, "Warehouse should not exist");
    }

    @Test
    public void saveAddsOrUpdatesAWarehouse() {
        Warehouse newWarehouse = new Warehouse(
                12345,
                "testName",
                "testLocation");

        // add the warehouse
        Warehouse savedWarehouse = warehouseRepository.save(newWarehouse);

        // check id generation
        assertEquals(savedWarehouse.getId(), 1005, "Warehouse should have the newly generated id");

        // find added warehouse and check values
        Warehouse addedWarehouse = warehouseRepository.findById(savedWarehouse.getId());
        assertEquals(addedWarehouse.getId(), savedWarehouse.getId(), "Warehouses should have the same id");
        assertEquals(addedWarehouse.getName(), savedWarehouse.getName(), "Warehouses should have the same name");
        assertEquals(addedWarehouse.getLocation(), savedWarehouse.getLocation(), "Warehouses should have the same location");

        // change the warehouse
        savedWarehouse.setName("nameTest");
        addedWarehouse = warehouseRepository.save(savedWarehouse);

        // check values again with change
        assertEquals(addedWarehouse.getName(), savedWarehouse.getName(), "Warehouses should have the same name");
        addedWarehouse = warehouseRepository.findById(savedWarehouse.getId());
        assertEquals(addedWarehouse.getName(), savedWarehouse.getName(), "Warehouses should have the same name");
    }
}
