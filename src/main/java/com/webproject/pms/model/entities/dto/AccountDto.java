package com.webproject.pms.model.entities.dto;

import java.math.BigDecimal;

public class AccountDto {
	
	private Long accountId;
	
	private String number;
	
	private BigDecimal balance;
	
	private String currency;
	
	private Boolean isBlocked;
	
	private Boolean isDeleted;
	
	public Long getAccountId() {
		return accountId;
	}
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Boolean getBlocked() {
		return isBlocked;
	}
	
	public void setBlocked(Boolean blocked) {
		isBlocked = blocked;
	}
	
	public Boolean getDeleted() {
		return isDeleted;
	}
	
	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}
}
