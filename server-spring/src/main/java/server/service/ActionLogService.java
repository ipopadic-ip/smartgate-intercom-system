package server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import server.dto.ActionLogDTO;
import server.dto.UserDTO;
import server.model.ActionLog;
import server.model.User;
import server.repository.ActionLogRepository;

@Service
public class ActionLogService extends BaseService<ActionLog, ActionLogDTO, Long>{

	@Autowired
	private ActionLogRepository actionLogRepository;
	
	@Override
	protected CrudRepository<ActionLog, Long> getRepository() {
		// TODO Auto-generated method stub
		return actionLogRepository;
	}

	@Override
	protected ActionLogDTO convertToDTO(ActionLog entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ActionLog convertToEntity(ActionLogDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateEntityFromDto(ActionLogDTO dto, ActionLog entity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ArrayList<ActionLogDTO> findAll(){
		
		List<ActionLog> logsEntity = new ArrayList<>();
		actionLogRepository.findAll().forEach(logsEntity::add);
		ArrayList<ActionLogDTO> logs = new ArrayList<>();
		
		for(ActionLog log : logsEntity) {
			logs.add(
					new ActionLogDTO(
							log.getId(),
							log.getAction(),
							log.getTimestamp(),
							new UserDTO(log.getUser().getId(), log.getUser().getUsername(), null, null, null, null),
							log.getDoorNumber(),
							true
							
						)
					);
		}
		
		return logs;
	} 
	
	@Override
    public ActionLogDTO save(ActionLogDTO dto) {

        ActionLog log = new ActionLog();

        log.setAction(dto.getAction());
        log.setTimestamp(dto.getTimestamp());

        if (dto.getUser() != null) {
            User user = new User();
            user.setId(dto.getUser().getId());
            log.setUser(user);
        }
        if (dto.getDoorNumber() == null) {
			log.setDoorNumber(0);
			
		}else {
	        log.setDoorNumber(dto.getDoorNumber());

		}
        
        ActionLog saved = actionLogRepository.save(log);

        return new ActionLogDTO(
                saved.getId(),
                saved.getAction(),
                saved.getTimestamp(),
                dto.getUser(),
                saved.getDoorNumber(),
                true
        );
    }


}
