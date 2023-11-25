package nl.solar.app.repositories;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Project;

/**
 * JPA implementation of the ProjectRepository.
 * 
 * @author Tim Knops
 */
@Repository("PROJECTS.JPA")
@Transactional
@Primary
public class ProjectRepositoryJpa implements EntityRepository<Project> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns a list of all projects.
     * 
     * @return List<Project> A list of all projects.
     */
    @Override
    public List<Project> findAll() {
        TypedQuery<Project> query = entityManager.createQuery("SELECT p FROM Project p", Project.class);
        return query.getResultList();
    }

    @Override
    public Project findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Project delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Project save(Project item) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}