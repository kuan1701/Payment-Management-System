package com.webproject.pms.dto;

public class BankCardPostDto {
	
	private Long cardId;
	
	private String number;
	
	private Integer CVV;
	
	private Boolean isActive;
	
	private String month;
	
	private String year;
	
	public Integer getCVV() {
		return CVV;
	}
	
	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}
	
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
