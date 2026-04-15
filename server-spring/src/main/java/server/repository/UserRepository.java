package server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import server.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
	Optional<User> findByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);
}
