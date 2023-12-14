package nl.solar.app.repositories.jpaRepositories;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.solar.app.repositories.TeamRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Team;

@Repository("TEAMS.JPA")
@Transactional
@Primary
public class TeamRepositoryJpa implements TeamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Team> findAll() {
        return entityManager.createQuery("SELECT t FROM Team t", Team.class).getResultList();
    }

    @Override
    public Team findById(long id) {
        return entityManager.find(Team.class, id);
    }

    @Override
    public Team delete(long id) {
        Team team = findById(id);
        if (team != null) {
            entityManager.remove(team);
        }
        return team;
    }

    @Override
    public Team save(Team team) {
        if (team.getId() == 0) {
            entityManager.persist(team);
        } else {
            team = entityManager.merge(team);
        }
        return team;
    }

    @Override
    public List<Team> findByWarehouseId(long warehouseId) {
        return entityManager
                .createQuery("SELECT t FROM Team t WHERE t.warehouse.id = :warehouse_id", Team.class)
                .setParameter("warehouse_id", warehouseId)
                .getResultList();
    }

    private List<Map<String, Object>> executeQuery(String queryString, String idField, String nameField) {
        List<Object[]> queryResult = entityManager.createQuery(queryString, Object[].class)
                .getResultList();

        // Format the query result to a list of maps.
        return queryResult.stream()
                .map(row -> Map.of("id", row[0], nameField, row[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getWarehousesInfo() {
        return executeQuery("SELECT w.id, w.name FROM Warehouse w", "warehouse", "warehouse");
    }
}