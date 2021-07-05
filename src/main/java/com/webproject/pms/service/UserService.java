package com.webproject.pms.service;

import com.webproject.pms.model.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {
	
	User saveUser(User user);
	
	boolean updateUser(User user, Long userId);
	
	User findUserByUserId(Long userId);
	
	User findUserByUserLogin(String login);
	
	User findUserByPhone(String phone);
	
	User findUserByEmail(String email);
	
	User findUserByPhoneAndEmail(String phone, String email);
	
	User findByActivationCode(String code);
	
	boolean registrationUser(User user, Model model);
	
	boolean activateUser(String code);
	
	boolean forgotPassword(String email, Model model);
	
	List<User> findAllUsers();
	
	List<User> searchByCriteria(String name, String surname, String phone, String email);
	
	UserDetails loadUserByUsername(String email);
}
