package server.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import server.dto.UserDTO;
import server.dto.UserRoleDTO;
import server.model.User;
import server.service.UserService;

@Controller
@RequestMapping("/api/user")

public class UserController extends BaseController<User, UserDTO, Long> {

    @Autowired
    private UserService UserService;

    @Override
    protected UserService getService() {
        return UserService;
    }
    
    
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {

            UserRoleDTO updatedUserRole = UserService.assignRole(userId, roleId);
            return ResponseEntity.ok(updatedUserRole);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Error: User or Role not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error assigning role.");
        }
    }

}
