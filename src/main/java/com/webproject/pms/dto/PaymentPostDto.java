package com.webproject.pms.dto;

import java.math.BigDecimal;

public class PaymentPostDto {
	
	private Long paymentId;
	
	private Long userId;
	
	private Boolean isOutgoing;
	
	private String senderNumber;
	
	private BigDecimal senderAmount;
	
	private String senderCurrency;
	
	private String recipientNumber;
	
	private BigDecimal recipientAmount;
	
	private String recipientCurrency;
	
	private BigDecimal exchangeRate;
	
	private BigDecimal newBalance;
	
	private String appointment;
	
	private String date;
	
	private Boolean condition;
	
	
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
	
	public String getRecipientNumber() {
		return recipientNumber;
	}
	
	public void setRecipientNumber(String recipientNumber) {
		this.recipientNumber = recipientNumber;
	}
	
	public BigDecimal getRecipientAmount() {
		return recipientAmount;
	}
	
	public void setRecipientAmount(BigDecimal recipientAmount) {
		this.recipientAmount = recipientAmount;
	}
	
	public String getRecipientCurrency() {
		return recipientCurrency;
	}
	
	public void setRecipientCurrency(String recipientCurrency) {
		this.recipientCurrency = recipientCurrency;
	}
	
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	public BigDecimal getNewBalance() {
		return newBalance;
	}
	
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}
	
	public String getAppointment() {
		return appointment;
	}
	
	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public Boolean getCondition() {
		return condition;
	}
	
	public void setCondition(Boolean condition) {
		this.condition = condition;
	}
}
