package server.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
	

	    private Long id;
	    

	    private String username;

	
	    private String password;

	    private Set<UserRoleDTO> userRole;
	    
	    private ApartmentDTO apartment;

	  
	    private Boolean active = true;
	    

}
