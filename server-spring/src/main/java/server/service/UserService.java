package server.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import server.dto.UserDTO;
import server.dto.UserRoleDTO;
import server.model.Role;
import server.model.User;
import server.model.UserRole;
import server.repository.RoleRepository;
import server.repository.UserRepository;
import server.repository.UserRoleRepository;

@Service
public class UserService extends BaseService<User, UserDTO, Long>{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	@Lazy
	private UserRoleService userRoleService;

	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
  @Override
 protected CrudRepository<User, Long> getRepository() {
      return userRepository;
  }
  
  @Transactional 
  @Override
  public List<UserDTO> findAll() {
      return StreamSupport.stream(getRepository().findAll().spliterator(), false)
              .map(this::convertToDTO)
              .collect(Collectors.toList());
  }
 
  
  @Override
  public UserDTO convertToDTO(User entity) {
      Set<UserRoleDTO> rolesDTO = new HashSet<>();
      if (entity.getUserRole() != null) {
          for (UserRole role : entity.getUserRole()) {
              rolesDTO.add(userRoleService.convertToDTO(role));
          }
      }

      return new UserDTO(
          entity.getId(),
          entity.getUsername(),
          null, 
          rolesDTO,
          entity.getActive()
      );
  }

  @Override
  protected User convertToEntity(UserDTO dto) {
      User user = new User();
      user.setId(dto.getId());
      user.setUsername(dto.getUsername());
      user.setPassword(passwordEncoder.encode(dto.getPassword()));  


      Set<UserRole> roles = new HashSet<>();
      if (dto.getUserRole() != null) {
          for (UserRoleDTO roleDTO : dto.getUserRole()) {
              UserRole ur = userRoleService.convertToEntity(roleDTO);
              ur.setUser(user); 
              roles.add(ur);
          }
      }

      user.setUserRole(roles);
      user.setActive(dto.getActive());

      return user;
  }
	
	public User findByUsernameAndPassword(String username, String password) {
		return this.userRepository.findByUsernameAndPassword(username, password).orElse(null);
	}
	
	@Transactional
	public User findByUsername(String username) {
		return this.userRepository.findByUsername(username).orElse(null);
	}


	@Transactional
    public User findByUsernameWithPrivileges(String username) {
        return userRepository.findByUsernameWithUserRole(username);
    }
	
	@Override
	protected void updateEntityFromDto(UserDTO dto, User entity) {
	    // Basic scalar updates
	    entity.setUsername(dto.getUsername());


	    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }	

	    entity.setActive(dto.getActive());

	    Map<Long, UserRole> existingRolesMap = entity.getUserRole()
	            .stream()
	            .filter(ur -> ur.getId() != null) 
	            .collect(Collectors.toMap(UserRole::getId, Function.identity()));

	    Set<Long> dtoRolesIds = new HashSet<>();
	    if (dto.getUserRole() != null) {
	        dtoRolesIds = dto.getUserRole().stream()
	                .filter(dtoRole -> dtoRole.getId() != null)
	                .map(UserRoleDTO::getId)
	                .collect(Collectors.toSet());
	    }

	    Iterator<UserRole> iterator = entity.getUserRole().iterator();
	    while (iterator.hasNext()) {
	        UserRole existingRole = iterator.next();
	        if (existingRole.getId() != null && !dtoRolesIds.contains(existingRole.getId())) {
	            iterator.remove(); 
	        }
	    }

	    if (dto.getUserRole() != null) {
	        for (UserRoleDTO dtoRole : dto.getUserRole()) {
	            UserRole ur;

	            if (dtoRole.getId() != null && existingRolesMap.containsKey(dtoRole.getId())) {
	                ur = existingRolesMap.get(dtoRole.getId());
	            } else {
	                ur = new UserRole();
	                entity.getUserRole().add(ur); 
	            }

	            ur.setActive(dtoRole.getActive());
	            ur.setUser(entity); 

	            if (dtoRole.getRole() != null && dtoRole.getRole().getId() != null) {
	                Role role = roleRepository
	                        .findById(dtoRole.getRole().getId())
	                        .orElse(null); 
	                ur.setRole(role);
	            }
	        }
	    }
	}
	@Transactional
	public UserRoleDTO assignRole(Long userId, Long roleId) {
	    User user = userRepository.findById(userId).orElseThrow();
	    Role role = roleRepository.findById(roleId).orElseThrow();

	    UserRole userRole = new UserRole();
	    userRole.setUser(user);
	    userRole.setRole(role);
	    userRole.setActive(true);

	    UserRole saved = userRoleRepository.save(userRole);
	    return userRoleService.convertToDTO(saved);
	}

	

}
