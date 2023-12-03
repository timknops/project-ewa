package nl.solar.app.repositories.jpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.solar.app.models.Product;
import nl.solar.app.repositories.EntityRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the repository for managing {@link Product} entities.
 * This repository provides CRUD operations for products using Java Persistence API (JPA).
 *
 * @author Julian Kruithof
 */
@Repository("PRODUCTS.JPA")
@Transactional
@Primary
public class ProductRepositoryJpa implements EntityRepository<Product> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the specified ID, or {@code null} if not found
     */
    @Override
    public Product findById(long id) {
        return entityManager.find(Product.class, id);
    }

    /**
     * Deletes a product by its ID from the database.
     *
     * @param id the ID of the product to delete
     * @return the deleted product, or {@code null} if the product with the specified ID does not exist
     */
    @Override
    public Product delete(long id) {
        Product toBeDelete = findById(id);

        if (toBeDelete == null) return null;

        entityManager.remove(toBeDelete);
        return toBeDelete;
    }

    /**
     * Saves or updates a product in the database.
     *
     * @param entity the product to save or update
     * @return the saved or updated product
     */
    @Override
    public Product save(Product entity) {
        return entityManager.merge(entity);
    }

}
