package server.service;

import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import server.model.UserRole;



@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


		server.model.User u = userService.findByUsernameWithPrivileges(username);
		if(u != null) {
			ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			for(UserRole userRole : ((server.model.User) u).getUserRole()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRole().getRole()));			
			}			
			
			return new User(u.getUsername(), u.getPassword(), grantedAuthorities);
		}
		
		throw new UsernameNotFoundException("User not found!");
	}

}