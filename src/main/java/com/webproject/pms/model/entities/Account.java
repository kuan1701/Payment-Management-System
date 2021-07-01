package com.webproject.pms.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	
	@NotEmpty
	@Size(min = 1, max = 255)
	private String number;
	
	@NotEmpty
	private BigDecimal balance;
	
	@NotEmpty
	private String currency;
	
	private Boolean isBlocked;
	
	private Boolean isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BankCard> bankCards;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Payment> payments;
	
	public Account() {
	}
	
	public Account(@NotEmpty @Size(min = 1, max = 255) String number,
	               @NotEmpty String currency) {
		this.number = number;
		this.currency = currency;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Account)) return false;
		Account account = (Account) o;
		return getAccountId().equals(account.getAccountId()) &&
				getNumber().equals(account.getNumber()) &&
				getBalance().equals(account.getBalance()) &&
				getCurrency().equals(account.getCurrency()) &&
				isBlocked.equals(account.isBlocked) &&
				isDeleted.equals(account.isDeleted) &&
				getUser().equals(account.getUser());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getAccountId(), getNumber(), getBalance(), getCurrency(), isBlocked, isDeleted, getUser());
	}
	
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
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
