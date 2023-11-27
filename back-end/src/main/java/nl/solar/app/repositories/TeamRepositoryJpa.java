package nl.solar.app.repositories;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Team;

@Repository("TEAMS.JPA")
@Transactional
@Primary
public class TeamRepositoryJpa implements EntityRepository<Team> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Team> findAll() {
        TypedQuery<Team> query = entityManager.createQuery("SELECT t FROM Team t", Team.class);
        return query.getResultList();
    }

    @Override
    public Team findById(long id) {
        return entityManager.find(Team.class, id);
    }

    @Override
    public Team delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Team save(Team item) {
        return entityManager.merge(item);
    }

}
