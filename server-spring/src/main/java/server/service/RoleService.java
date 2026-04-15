package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import server.dto.RoleDTO;
import server.model.Role;
import server.repository.RoleRepository;

@Service
public class RoleService extends BaseService<Role, RoleDTO, Long>{

	 @Autowired
	    private RoleRepository roleRepository;

	    @Override
	    protected CrudRepository<Role, Long> getRepository() {
	        return roleRepository;
	    }

	    @Override
	    protected RoleDTO convertToDTO(Role entity) {
	        return new RoleDTO(entity.getId(), entity.getRole(),null, entity.getActive());
	    }

	    @Override
	    protected Role convertToEntity(RoleDTO dto) {
	        return new Role(dto.getId(), dto.getRole(), null, dto.getActive());
	    }

	    @Override
	    protected void updateEntityFromDto(RoleDTO dto, Role entity) {
	        entity.setRole(dto.getRole());
	        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
	    }
	    
	    
	    public RoleDTO findByNaziv(String role) {
	        return roleRepository.findByRole(role)
	                .map(this::convertToDTO)
	                .orElse(null);
	    }
	}
