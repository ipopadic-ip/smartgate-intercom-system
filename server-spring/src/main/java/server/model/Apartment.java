package server.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Integer doorNumber;
    
    @OneToMany(mappedBy = "apartment")
    private List<User> users = new ArrayList<User>();

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE") 
    private Boolean active = true;
    
    
    
    
    public Apartment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(Integer doorNumber) {
		this.doorNumber = doorNumber;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

    
    
}
