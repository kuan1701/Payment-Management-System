package com.webproject.pms.model.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
	
	@Id
	private Long id;
	
	private String name;
	
	@Transient
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> users;
	
	public Role() {
	}
	
	public Role(Long id) {
		this.id = id;
	}
	
	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@Override
	public String getAuthority() {
		return getName();
	}
}
