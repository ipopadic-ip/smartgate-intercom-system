package server.dto;

import java.util.ArrayList;
import java.util.List;

public class ApartmentDTO {

	private Long id;
    
    private int doorNumber;
    
    private List<UserDTO> users = new ArrayList<UserDTO>();
    
    private Boolean active;

	public ApartmentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApartmentDTO(Long id, int doorNumber, List<UserDTO> users) {
		super();
		this.id = id;
		this.doorNumber = doorNumber;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(int doorNumber) {
		this.doorNumber = doorNumber;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
}
