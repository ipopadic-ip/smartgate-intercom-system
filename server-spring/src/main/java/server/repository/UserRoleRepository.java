package server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import server.model.UserRole;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long>{

}
