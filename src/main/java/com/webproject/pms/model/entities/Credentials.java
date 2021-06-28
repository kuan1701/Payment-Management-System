package com.webproject.pms.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Credentials {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String password;
	
	private Boolean active;
	
	private Date creationDate;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	
	public Credentials() {
	}
	
	public Credentials(Long id, String password, Boolean active, Date creationDate, User user) {
		this.id = id;
		this.password = password;
		this.active = active;
		this.creationDate = creationDate;
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
