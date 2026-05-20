package server.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.dto.ApartmentDTO;
import server.dto.RoleDTO;
import server.dto.UserDTO;
import server.dto.UserRoleDTO;
import server.model.User;
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

    
    @PostMapping("/register")
    public ResponseEntity<?> addEntity() {
        System.out.println("HIT /all POST");
    	for (int i = 0; i < 5; i++) {
    		ApartmentDTO a = apartmentService.save(new ApartmentDTO(null, i+10, new ArrayList<UserDTO>()));
    		UserDTO u = userService.save(new UserDTO(
    				null,
    				"korisnik"+ i , 
    				"korisnik"+i,
    				new HashSet<UserRoleDTO>(),
    				true
    				));
            RoleDTO r = roleService.findById(2L).orElse(null);    	


            userService.assignRole(u.getId(), r.getId());    	
    		userService.assignApartment(u.getId(), a.getId());
		}
    	
    	return ResponseEntity.ok().build();    
    }

        
}
