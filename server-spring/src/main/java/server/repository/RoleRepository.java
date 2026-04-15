package server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import server.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Optional<Role> findByRole(String role);

}
