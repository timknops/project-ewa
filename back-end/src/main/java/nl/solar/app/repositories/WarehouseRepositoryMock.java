package nl.solar.app.repositories;

import nl.solar.app.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("WAREHOUSES.INMEMORY")
public class WarehouseRepositoryMock implements EntityRepository<Warehouse>{
    private List<Warehouse> warehouses;
    private final long ID = 3000;
    private long currentId = ID;

    private final String[] names = {
            "Solar Sedum",
            "Superzon",
            "The switch",
            "Induct",
            "EHES"
    };

    private final String[] locations = {
            "H.J.E. Wenckebachweg 47D, 1096 AK Amsterdam",
            "Marconistraat 4A, 1704 RG Heerhugowaard",
            "Barndegat 8, 1505 HN Zaandam",
            "Philippusweg 2, 3125 AS Schiedam",
            "Bolwerk 5, 3905 NH Veenendaal"
    };

    public WarehouseRepositoryMock() {
        int counter = 0;
        warehouses = new ArrayList<>();
        for (String name : names){
            warehouses.add(Warehouse.createDummyWarehouses(
               currentId,
               name,
               locations[counter]
            ));
            currentId += 3;
            counter++;
        }

    }

    @Override
    public List<Warehouse> findALL() {
        return warehouses;
    }

    @Override
    public Warehouse findById(long id) {
        return warehouses.stream().filter(warehouse -> warehouse.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Warehouse delete(long id) {
        Warehouse warehouseToDelete = this.findById(id);

        if (warehouseToDelete != null){
            warehouses.remove(warehouseToDelete);
        }

        return warehouseToDelete;
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        int i = warehouses.indexOf(warehouse);
        if (i != -1){
            warehouses.set(i,warehouse);
        } else {
            if (warehouse.getId() == 0) {
                warehouse.setId(currentId);
                currentId += 3;
            }
            warehouses.add(warehouse);
        }

        return warehouse;
    }
}
