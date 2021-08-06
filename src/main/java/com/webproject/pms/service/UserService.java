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
	
	User saveUser(User user);

	boolean deleteUser(User user);
	
	boolean updateUser(User user, Long id, String name, String surname, String phone, String email, String password);

	boolean updatePassword(User user, String password);
	
	User findUserByUserId(Long userId);
	
	User findUserByUsername(String username);
	
	User findUserByPhone(String phone);
	
	User findUserByEmail(String email);
	
	User findByActivationCode(String code);
	
	boolean registrationUser(User user, Model model, String siteURL) throws UnsupportedEncodingException, MessagingException;

	boolean adminCreateUser(User user, Model model, String siteURL) throws UnsupportedEncodingException, MessagingException;
	
	boolean activateUser(String code);
	
	List<User> findAllUsers();
	
	List<User> searchByCriteria(String name, String surname, String phone, String email);
	
	UserDetails loadUserByUsername(String email);
	
	void updateResetPasswordToken(String token, String email) throws UserPrincipalNotFoundException, UserNotFoundException;
	
	User getByResetPasswordToken(String token);

	void processOAuthPostLogin(String username);
}
