package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.solar.app.models.User;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository("USER.JPA")
@Transactional
public class UserRepositoryJpa implements EntityRepository<User> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User delete(long id) {
        User deletedUser = findById(id);
        if (deletedUser == null){
            return null;
        }
        entityManager.remove(deletedUser);
        return deletedUser;
    }

    @Override
    public User save(User item) {
        return entityManager.merge(item); }
}
