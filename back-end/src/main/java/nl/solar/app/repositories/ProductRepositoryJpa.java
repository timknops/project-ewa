package nl.solar.app.repositories;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Product;

@Repository("PRODUCTS.JPA")
@Transactional
@Primary
public class ProductRepositoryJpa implements EntityRepository<Product> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    @Override
    public Product findById(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Product delete(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Product save(Product entity) {
        return entityManager.merge(entity);
    }

}
