package server.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDTO {
	
    private Long id;

	private String role;

	private Set<UserRoleDTO> userRole;
	

    private Boolean active = true;
    
    

}
