package com.webproject.pms.dto;

import com.webproject.pms.model.entities.Role;

public class UserPostDto {
	
	private Long userId;
	
	private String name;
	
	private String surname;
	
	private String phone;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private String registrationDate;
	
	private Role role;
	
	public UserPostDto(String name, String surname, String phone, String email, String username, String password) {
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.email = email;
		this.username = username;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRegistrationDate() {
		return registrationDate;
	}
	
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
}
