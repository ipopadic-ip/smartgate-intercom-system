package server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import server.model.Apartment;

@Repository
public interface ApartmentRepository  extends CrudRepository<Apartment, Long> {

}
