package com.webproject.pms.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	
	private Long userId;
	
	private Boolean isOutgoing;
	
	@NotEmpty
	@Size(min = 1, max = 20)
	private String senderNumber;
	
	private BigDecimal senderAmount;
	
	private String senderCurrency;
	
	@NotEmpty
	@Size(min = 1, max = 20)
	private String recipientNumber;
	
	private BigDecimal recipientAmount;
	
	private String recipientCurrency;
	
	private BigDecimal exchangeRate;
	
	private BigDecimal newBalance;
	
	private String appointment;
	
	private String date;
	
	private Boolean status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	public Payment() {
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Payment)) return false;
		Payment payment = (Payment) o;
		return paymentId.equals(payment.paymentId) &&
				isOutgoing.equals(payment.isOutgoing) &&
				senderNumber.equals(payment.senderNumber) &&
				senderAmount.equals(payment.senderAmount) &&
				senderCurrency.equals(payment.senderCurrency) &&
				recipientNumber.equals(payment.recipientNumber) &&
				recipientAmount.equals(payment.recipientAmount) &&
				recipientCurrency.equals(payment.recipientCurrency) &&
				exchangeRate.equals(payment.exchangeRate) &&
				newBalance.equals(payment.newBalance) &&
				appointment.equals(payment.appointment) &&
				date.equals(payment.date) &&
				status.equals(payment.status) &&
				account.equals(payment.account);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(paymentId, isOutgoing, senderNumber, senderAmount, senderCurrency, recipientNumber, recipientAmount, recipientCurrency, exchangeRate, newBalance, appointment, date, status, account);
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getPaymentId() {
		return paymentId;
	}
	
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
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
		return status;
	}
	
	public void setCondition(Boolean status) {
		this.status = status;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
}
