package com.webproject.pms.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "bank_card")
public class BankCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;
	
	@NotEmpty
	@Size(max = 19)
	private String number;
	
	@NotEmpty
	@Size(max = 3)
	private String CVV;
	
	@NotEmpty
	@Size(max = 2)
	private String month;
	
	@NotEmpty
	@Size(max = 2)
	private String year;
	
	private String validity;
	
	private Boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	public BankCard() {
	}
	
	public BankCard(@NotEmpty @Size(max = 19) String number,
	                @NotEmpty @Size(max = 3) String CVV) {
		this.number = number;
		this.CVV = CVV;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BankCard)) return false;
		BankCard bankCard = (BankCard) o;
		return cardId.equals(bankCard.cardId) &&
				number.equals(bankCard.number) &&
				CVV.equals(bankCard.CVV) &&
				validity.equals(bankCard.validity) &&
				isActive.equals(bankCard.isActive) &&
				account.equals(bankCard.account);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cardId, number, CVV, validity, isActive, account);
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
	
	public String getCVV() {
		return CVV;
	}
	
	public void setCVV(String CVV) {
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
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
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
