package nl.solar.app.repositories;

import java.util.List;

public interface EntityRepository<E> {

    List<E> findAll(); //find all entities

    E findById(long id); //find an entity via its id

    E delete(long id); // delete an entity via its id

    E save(E item);
}
