package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.ForecastItem;
import nl.solar.app.models.Warehouse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DASHBOARD.JPA")
@Transactional
@Primary
public class DashboardRepositoryJpa {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ForecastItem> findAll() {
        return entityManager.createQuery("query here", ForecastItem.class).getResultList();
    }
}
