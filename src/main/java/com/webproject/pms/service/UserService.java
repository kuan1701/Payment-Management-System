package com.webproject.pms.service;

import com.webproject.pms.exception.UserNotFoundException;
import com.webproject.pms.model.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;


public interface UserService {

	Boolean saveUser(User user);

	Boolean deleteUser(User user);
	
	Boolean updateUser(User user, Long id, String name, String surname, String phone, String email, String password);

	Boolean updatePassword(User user, String password);
	
	User findUserByUserId(Long userId);
	
	User findUserByUsername(String username);
	
	User findUserByPhone(String phone);
	
	User findUserByEmail(String email);
	
	User findByActivationCode(String code);
	
	Boolean registrationUser(User user, String siteURL) throws UnsupportedEncodingException, MessagingException;

	Boolean adminCreateUser(User user, String siteURL) throws UnsupportedEncodingException, MessagingException;
	
	Boolean activateUser(String code);
	
	List<User> findAllUsers();
	
	List<User> searchByCriteria(String name, String surname, String phone, String email);
	
	UserDetails loadUserByUsername(String email);
	
	void updateResetPasswordToken(String token, String email) throws UserPrincipalNotFoundException, UserNotFoundException;
	
	User getByResetPasswordToken(String token);

	void processOAuthPostLogin(String username);
}
