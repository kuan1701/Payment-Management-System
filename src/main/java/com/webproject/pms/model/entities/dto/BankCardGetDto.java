package com.webproject.pms.model.entities.dto;

public class BankCardGetDto {
	
	private Long cardId;
	
	private String number;
	
	private Integer CVV;
	
	private String validity;
	
	private Boolean isActive;
	
	private String month;
	
	private String year;
	
	
	public Long getCardId() {
		return cardId;
	}
	
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public Integer getCVV() {
		return CVV;
	}
	
	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}
	
	public String getValidity() {
		return validity;
	}
	
	public void setValidity(String validity) {
		this.validity = validity;
	}
	
	public Boolean getActive() {
		return isActive;
	}
	
	public void setActive(Boolean active) {
		isActive = active;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
}
