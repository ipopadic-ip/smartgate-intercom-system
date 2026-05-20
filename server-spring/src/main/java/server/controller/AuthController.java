package server.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.PermitAll;
import server.dto.AuthResponse;
import server.dto.RoleDTO;
import server.dto.UserDTO;
import server.dto.UserLoginDTO;
import server.dto.UserRoleDTO;
import server.model.User;
import server.service.RoleService;
import server.service.UserService;
import server.utils.TokenUtils;

@RestController
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
	public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDTO user) {

	    User u = userService.findByUsername(user.getUsername());
	    if (u == null) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }


	    boolean passwordMatches =
	            passwordEncoder.matches(user.getPassword(), u.getPassword());

	    if (passwordMatches) {

	        UserDetails userDetails =
	                userDetailsService.loadUserByUsername(user.getUsername());

	        Map<String, Object> claims = new HashMap<>();

	        if(u.getApartment() != null) {
		        claims.put("stan", u.getApartment().getDoorNumber());
	        }
	        String token = tokenUtils.generateToken(userDetails, claims);
	        //return ResponseEntity.ok(new AuthResponse("TEST123"));

	        return ResponseEntity.ok(new AuthResponse(token));
	    }

	    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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