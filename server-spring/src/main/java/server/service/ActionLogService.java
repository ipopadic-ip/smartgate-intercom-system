package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import server.dto.ActionLogDTO;
import server.model.ActionLog;
import server.repository.ActionLogRepository;

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


}
