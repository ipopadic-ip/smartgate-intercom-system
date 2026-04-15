package server.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoleDTO {
    private Long id;

    
    private UserDTO user;

    
    private RoleDTO role;
    
    
    private Boolean active = true;
}
