package server.dto;

import java.util.Set;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import server.model.UserRole;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
	

	    private Long id;

	    

	    private String username;

	
	    private String password;

	    private Set<UserRoleDTO> userRole;

	  
	    private Boolean active = true;
	    

}
