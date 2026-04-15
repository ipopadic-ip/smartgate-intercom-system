package server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import server.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Optional<Role> findByRole(String role);

}
