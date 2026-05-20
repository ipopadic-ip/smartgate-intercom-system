package server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import server.model.ActionLog;

@Repository
public interface ActionLogRepository  extends CrudRepository<ActionLog, Long> {

}
