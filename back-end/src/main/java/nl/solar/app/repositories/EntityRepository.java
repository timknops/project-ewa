package nl.solar.app.repositories;

import java.util.List;

public interface EntityRepository<E> {

    List<E> findALL(); //find all entities

    E findById(long id); //find an entity via its id

    E delete(long id); // delete an entity via its id

    E update(E object); //update an entity if it exists, check via object.id

    E add(E object); //add a new entity to the list
}
