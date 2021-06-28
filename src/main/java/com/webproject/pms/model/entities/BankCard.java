package com.webproject.pms.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class BankCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;
	
	@NotEmpty
	@Size(min = 16, max = 16)
	private String number;
	
	@NotEmpty
	@Size(min = 3, max = 3)
	private Integer CVV;
	
	private String validity;
	
	private Boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	public BankCard() {
	}
	
	public BankCard(@NotEmpty @Size(min = 16, max = 16) String number,
	                @NotEmpty @Size(min = 3, max = 3) Integer CVV) {
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
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
}
