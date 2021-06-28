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
public class User implements Serializable, UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotEmpty
	@Size(min = 2, max = 30)
	private String name;
	
	@NotEmpty
	@Size(min = 2, max = 30)
	private String surname;
	
	@NotEmpty
	@Size(min = 7, max = 30)
	private String phone;
	
	@NotEmpty
	@Email
	private String email;
	
	private String activationCode;
	
	@NotEmpty
	@Size(min = 2, max = 30)
	private String login;
	
	@NotEmpty
	@Size(min = 8, max = 100)
	private String password;
	
	@Transient
	private String repeatedPassword;
	
	@Transient
	private String verifiedPassword;
	
	@NotEmpty
	private String registrationDate;
	
	@NotEmpty
	private String googleName;
	
	@NotEmpty
	private String googleUserName;
	
	private Boolean active;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Role role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Account> accounts;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Letter> letters;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LogEntry> logEntries;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Credentials> credentials;
	
	public User() {
	}
	
	public User(String name,
	            String surname,
	            String email,
	            String password,
	            String activationCode,
	            Boolean active
	) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.activationCode = activationCode;
		this.active = active;
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
				registrationDate.equals(user.registrationDate) &&
				googleName.equals(user.googleName) &&
				googleUserName.equals(user.googleUserName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userId, name, surname, phone, email, login, getPassword(), registrationDate, googleName, googleUserName);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(getRole().getName()));
	}
	
	@Override
	public String getUsername() {
		return email;
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
	
	public String getGoogleName() {
		return googleName;
	}
	
	public void setGoogleName(String googleName) {
		this.googleName = googleName;
	}
	
	public String getGoogleUserName() {
		return googleUserName;
	}
	
	public void setGoogleUserName(String googleUserName) {
		this.googleUserName = googleUserName;
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
	
	public String getVerifiedPassword() {
		return verifiedPassword;
	}
	
	public void setVerifiedPassword(String verifiedPassword) {
		this.verifiedPassword = verifiedPassword;
	}
	
	public String getActivationCode() {
		return activationCode;
	}
	
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public List<Credentials> getCredentials() {
		return credentials;
	}
	
	public void setCredentials(List<Credentials> credentials) {
		this.credentials = credentials;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
}