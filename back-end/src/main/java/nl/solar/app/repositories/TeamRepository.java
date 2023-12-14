package nl.solar.app.repositories;

import nl.solar.app.models.Team;

import java.util.List;
import java.util.Map;

public interface TeamRepository extends EntityRepository<Team>{

    List<Team> findByWarehouseId(long warehouseId);
    List<Map<String, Object>> getWarehousesInfo();
}