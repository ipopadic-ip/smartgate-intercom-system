package server.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.security.PermitAll;
import server.dto.UserRoleDTO;
import server.dto.UserDTO;
import server.dto.RoleDTO;
import server.dto.UserLoginDTO;

import server.model.User;

import server.service.UserService;
import server.service.RoleService;
import server.utils.TokenUtils;

@Controller
@RequestMapping("/api/auth")
@PermitAll
public class AuthController {
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	



	

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO user) {


        User u = userService.findByUsername(user.getUsername());

        if (u == null) {
            System.out.println("User not found.");
            return new ResponseEntity<>("User not found.", HttpStatus.UNAUTHORIZED);
        }


        boolean passwordMatches = passwordEncoder.matches(user.getPassword(), u.getPassword());

        if (passwordMatches) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = tokenUtils.generateToken(userDetails);
            return ResponseEntity.ok(token);
        }

        return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
    }
	
	
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDTO userDTO) {

        RoleDTO adminRole = roleService.findByNaziv("ROLE_ADMIN");
        if (adminRole == null) {

        	adminRole = roleService.save(new RoleDTO(null,"ROLE_ADMIN",null, true));
        }


        UserRoleDTO userRole = new UserRoleDTO();
        userRole.setActive(true);
        userRole.setRole(adminRole);


        Set<UserRoleDTO> prava = new HashSet<>();
        prava.add(userRole);
        userDTO.setUserRole(prava);


        UserDTO saved = userService.save(userDTO);

        return ResponseEntity.ok(saved);
    }
}