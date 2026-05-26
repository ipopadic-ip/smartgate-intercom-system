package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import server.dto.ApartmentDTO;
import server.model.Apartment;
import server.repository.ApartmentRepository;

@Service
public class ApartmentService extends BaseService<Apartment, ApartmentDTO, Long>{

	 @Autowired
	    private ApartmentRepository apartmentRepository;

	    @Override
	    protected CrudRepository<Apartment, Long> getRepository() {
	        return apartmentRepository;
	    }
	    
	    public ApartmentDTO findApartmentById(Long id) {
	        Apartment entity = getRepository().findById(id).orElse(null);
	        return convertToDTO(entity);
	    }

	    @Override
	    protected ApartmentDTO convertToDTO(Apartment entity) {
	        if (entity == null) return null;

	        return new ApartmentDTO(
	            entity.getId(),
	            entity.getDoorNumber(),
	            null
	        );
	    }

		@Override
		protected Apartment convertToEntity(ApartmentDTO dto) {
		    if (dto == null) return null;

		    Apartment a = new Apartment();
		    a.setId(dto.getId());
		    a.setDoorNumber(dto.getDoorNumber());
		    a.setActive(dto.getActive());

		    return a;
		}

		@Override
		protected void updateEntityFromDto(ApartmentDTO dto, Apartment entity) {
			// TODO Auto-generated method stub
			
		}


}
