package com.webproject.pms.model.entities.enums;

public enum Role {
	
	ADMIN("Admin"),
	USER("User"),
	GUEST("Guest");
	
	private String name;
	
	Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
