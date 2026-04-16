package server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	Optional<User> findByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.userRole ur LEFT JOIN FETCH ur.role WHERE u.username = :username")
    User findByUsernameWithUserRole(@Param("username") String username);
}
