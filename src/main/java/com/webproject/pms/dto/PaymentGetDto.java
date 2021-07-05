package com.webproject.pms.dto;

import java.math.BigDecimal;

public class PaymentGetDto {
	
	private Long paymentId;
	
	private Long userId;
	
	private Boolean isOutgoing;
	
	private String senderNumber;
	
	private String recipientNumber;
	
	private BigDecimal senderAmount;
	
	private BigDecimal recipientAmount;
	
	private String senderCurrency;
	
	private BigDecimal exchangeRate;
	
	private String name;
	
	private String surname;
	
	private BigDecimal newBalance;
	
	private String date;
	
	
	public Long getPaymentId() {
		return paymentId;
	}
	
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	
	public Long getAccountId() {
		return userId;
	}
	
	public void setAccountId(Long userId) {
		this.userId = userId;
	}
	
	public Boolean getOutgoing() {
		return isOutgoing;
	}
	
	public void setOutgoing(Boolean outgoing) {
		isOutgoing = outgoing;
	}
	
	public String getSenderNumber() {
		return senderNumber;
	}
	
	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}
	
	public String getRecipientNumber() {
		return recipientNumber;
	}
	
	public void setRecipientNumber(String recipientNumber) {
		this.recipientNumber = recipientNumber;
	}
	
	public BigDecimal getSenderAmount() {
		return senderAmount;
	}
	
	public void setSenderAmount(BigDecimal senderAmount) {
		this.senderAmount = senderAmount;
	}
	
	public String getSenderCurrency() {
		return senderCurrency;
	}
	
	public void setSenderCurrency(String senderCurrency) {
		this.senderCurrency = senderCurrency;
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
	
	public BigDecimal getNewBalance() {
		return newBalance;
	}
	
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public BigDecimal getRecipientAmount() {
		return recipientAmount;
	}
	
	public void setRecipientAmount(BigDecimal recipientAmount) {
		this.recipientAmount = recipientAmount;
	}
	
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
