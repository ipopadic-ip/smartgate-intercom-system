package server.dto;

import java.time.LocalDateTime;

public class ActionLogDTO {
	
    private Long id;

    private String action;

    private LocalDateTime timestamp;
    
    private UserDTO user;
    
    private Integer doorNumber;
    
    private Boolean active;

	public ActionLogDTO() {
		super();
		// TODO Auto-generated constructor stub
	}





	public ActionLogDTO(Long id, String action, LocalDateTime timestamp, UserDTO user, Integer doorNumber, Boolean active) {
		super();
		this.id = id;
		this.action = action;
		this.timestamp = timestamp;
		this.user = user;
		this.doorNumber = doorNumber;
		this.active = active;
	}





	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}



	public Integer getDoorNumber() {
		return doorNumber;
	}



	public void setDoorNumber(Integer doorNumber) {
		this.doorNumber = doorNumber;
	}



	public Boolean getActive() {
		return active;
	}



	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
    

}
