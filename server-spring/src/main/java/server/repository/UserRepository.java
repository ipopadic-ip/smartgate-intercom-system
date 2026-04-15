package server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import server.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	Optional<User> findByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);
}
