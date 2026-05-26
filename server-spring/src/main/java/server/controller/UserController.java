package server.controller;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.dto.ActionLogDTO;
import server.dto.ApartmentDTO;
import server.dto.RoleDTO;
import server.dto.UserDTO;
import server.dto.UserRoleDTO;
import server.model.User;
import server.service.ActionLogService;
import server.service.ApartmentService;
import server.service.RoleService;
import server.service.UserService;

@RestController
@RequestMapping("/api/user")

public class UserController extends BaseController<User, UserDTO, Long> {

    @Autowired
    private UserService userService;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ActionLogService actionLogService;

    @Override
    protected UserService getService() {
        return userService;
    }
    
    
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {

            UserRoleDTO updatedUserRole = userService.assignRole(userId, roleId);
            return ResponseEntity.ok(updatedUserRole);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Error: User or Role not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error assigning role.");
        }
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('TENANT')")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id,
            @RequestBody UserDTO dto,
            Authentication auth
    ) {

        User currentUser = userService.findByUsername(auth.getName());
        

        if ( !currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(userService.update(id, dto));
    }

    
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ActionLogDTO>> getAdminLogs(){
    	
    	List<ActionLogDTO> logs =  actionLogService.findAll();

		return ResponseEntity.ok(logs);
	}
    
    @GetMapping("/apartments")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ApartmentDTO>> getApartments(){
    	
    	List<ApartmentDTO> apartments =  apartmentService.findAll();

		return ResponseEntity.ok(apartments);
	}
    
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser( @RequestBody UserDTO dto, Authentication auth){
    	    	
    	RoleDTO roleDTO = roleService.findByNaziv("ROLE_TENANT");
    	ApartmentDTO apartmentDTO = apartmentService.findApartmentById(dto.getApartment().getId());
    	
    	UserDTO userDTO = new UserDTO();
    	
    	userDTO.setUsername(dto.getUsername());
    	userDTO.setPassword(dto.getPassword());
    	userDTO.setActive(true);
    	userDTO.setApartment(new ApartmentDTO(apartmentDTO.getId(), apartmentDTO.getDoorNumber(), null));
    	userDTO.setId(null);
    	
    	UserRoleDTO userRoleDTO = new UserRoleDTO();
    	userRoleDTO.setRole(roleDTO);
    	userRoleDTO.setActive(true);
    	userRoleDTO.setUser(userDTO);
    	
    	
    	Set<UserRoleDTO> userRoles = new HashSet<>();
    	userRoles.add(userRoleDTO);
    	userDTO.setUserRole(userRoles);
    	
    	UserDTO saved = userService.save(userDTO);

		return ResponseEntity.ok(saved);
	}    
    
    @PutMapping("/activate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> activateUser( @PathVariable Long id, Authentication auth){
    	
    	UserDTO userDTO = userService.findUserById(id);
    	userDTO.setActive(true);
    	UserDTO updated = userService.update(id, userDTO);
    	
		return ResponseEntity.ok(updated);
	}
    
    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivateUser( @PathVariable Long id, Authentication auth){
    	
    	UserDTO userDTO = userService.findUserById(id);
    	userDTO.setActive(false);
    	UserDTO updated = userService.update(id, userDTO);
    	
		return ResponseEntity.ok(updated);
	}
    
    @GetMapping("/everyone")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
		
		List<UserDTO> users = userService.findAll();
		
		return ResponseEntity.ok(users);
    }
    



        
}
