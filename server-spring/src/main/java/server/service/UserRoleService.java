package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;

import server.dto.UserRoleDTO;
import server.model.Role;
import server.model.User;
import server.model.UserRole;
import server.repository.UserRoleRepository;

public class UserRoleService extends BaseService<UserRole, UserRoleDTO, Long> {

	  @Autowired
	    private UserRoleRepository userRoleRepository;

	    @Autowired
	    @Lazy
	    private UserService userService;

	    @Autowired
	    @Lazy
	    private RoleService roleService;

	    @Override
	    protected CrudRepository<UserRole, Long> getRepository() {
	        return userRoleRepository;
	    }

	    @Override
	    protected UserRoleDTO convertToDTO(UserRole entity) {
	        return new UserRoleDTO(
	            entity.getId(),
	            null, 
	            roleService.convertToDTO(entity.getRole()),
	            entity.getActive()
	        );
	    }

	    @Override
	    protected UserRole convertToEntity(UserRoleDTO dto) {
	        UserRole ur = new UserRole();
	        ur.setId(dto.getId());
	        ur.setActive(true);

	        if (dto.getRole() != null && dto.getRole().getId() != null) {
	            Role r = new Role();
	            r.setId(dto.getRole().getId());
	            ur.setRole(r);
	        }

	        if (dto.getUser() != null && dto.getUser().getId() != null) {
	            User u = new User();
	            u.setId(dto.getUser().getId());
	            ur.setUser(u);
	        }

	        return ur;
	    }

	    protected void updateEntityFromDto(UserRoleDTO dto, UserRole entity) {
	        entity.setActive(dto.getActive());

	        if (dto.getRole() != null && dto.getRole().getId() != null) {
	            Role role = new Role();
	            role.setId(dto.getRole().getId());
	            entity.setRole(role);
	        }

	        if (dto.getUser() != null && dto.getUser().getId() != null) {
	            User user = new User();
	            user.setId(dto.getUser().getId());
	            entity.setUser(user);
	        }
	    }
	}
