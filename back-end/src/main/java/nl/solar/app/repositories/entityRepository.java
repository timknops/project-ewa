package nl.solar.app.repositories;

import java.util.List;

public interface entityRepository<E> {

    List<E> findALL(); //find all entities

    E findById(long id); //find an entity via its id

    E delete(long id); // delete an entity via its id

    E save(E item);
}
