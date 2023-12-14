package nl.solar.app.repositories;

import nl.solar.app.models.Team;

import java.util.List;

public interface TeamRepository extends EntityRepository<Team>{

    List<Team> findByWarehouseId(long warehouseId);
}