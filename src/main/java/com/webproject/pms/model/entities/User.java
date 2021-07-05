package com.webproject.pms.model.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Serializable, UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotEmpty
	@Size(min = 2)
	private String name;
	
	@NotEmpty
	@Size(min = 2)
	private String surname;
	
	@NotEmpty
	@Size(min = 7)
	private String phone;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	@Size(min = 2)
	private String login;
	
	@NotEmpty
	@Size(min = 6)
	private String password;
	
	@Transient
	private String repeatedPassword;
	
	private Boolean emailVerified;
	
	private String activationCode;
	
	private String registrationDate;
	
	private Boolean active;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Role role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Account> accounts;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Letter> letters;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LogEntry> logEntries;

	public User() {
	}
	
	public User(@NotEmpty @Size(min = 2) String name,
	            @NotEmpty @Size(min = 2) String surname,
	            @NotEmpty @Email String email,
	            Boolean emailVerified) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.emailVerified = emailVerified;
	}
	
	public User(@NotEmpty @Size(min = 2) String name,
	            @NotEmpty @Size(min = 2) String surname,
	            @NotEmpty @Size(min = 7) String phone,
	            @NotEmpty @Email String email,
	            @NotEmpty @Size(min = 2) String login
	) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.login = login;
		this.phone = phone;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User user = (User) o;
		return userId.equals(user.userId) &&
				name.equals(user.name) &&
				surname.equals(user.surname) &&
				phone.equals(user.phone) &&
				email.equals(user.email) &&
				login.equals(user.login) &&
				getPassword().equals(user.getPassword()) &&
				registrationDate.equals(user.registrationDate);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userId, name, surname, phone, email, login, getPassword(), registrationDate);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(getRole().getName()));
	}
	
	@Override
	public String getUsername() {
		return login;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getRegistrationDate() {
		return registrationDate;
	}
	
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRepeatedPassword() {
		return repeatedPassword;
	}
	
	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public Boolean getEmailVerified() {
		return emailVerified;
	}
	
	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	
	public String getActivationCode() {
		return activationCode;
	}
	
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	public List<Letter> getLetters() {
		return letters;
	}
	
	public void setLetters(List<Letter> letters) {
		this.letters = letters;
	}
	
	public List<LogEntry> getLogEntries() {
		return logEntries;
	}
	
	public void setLogEntries(List<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}
}