package server.dto;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.UserRole;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDTO {
	
    private Long id;

	private String role;

	private Set<UserRoleDTO> userRole;
	

    private Boolean active = true;

}
