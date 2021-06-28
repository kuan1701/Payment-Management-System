package com.webproject.pms.service;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.entities.dto.UserGetDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {
	
	User saveUser(User user);
	
	User findByActivationCode(String code);
	
	User findUserByUserId(Long userId);
	
	User findUserByUserLogin(String login);
	
	UserGetDto findUserByPhone(String phone);
	
	UserGetDto findUserByEmail(String email);
	
	UserGetDto findUserByActivationCode(String code);
	
	UserGetDto findUserByGoogleUserName(String googleUsername);
	
	UserGetDto findUserByPhoneAndEmail(String phone, String email);
	
	void coderPassword(User user);
	
	void addCredentialsUser(User user);
	
	void activateCodeForNewPassword(Model model, String code);
	
	boolean activate(String code);
	
	boolean addUser(User user, Model model);
	
	boolean deleteUserByUserId(Long userId);
	
	boolean updateUser(User user, Long userId);
	
	boolean checkEmail(User user, Model model);
	
	boolean checkPassword(User user, Model model);
	
	boolean checkRepeatedPassword(User user, Model model);
	
	boolean registrationUser(User user, Model model);
	
	boolean forgotPassword(String email, Model model);
	
	boolean notFoundUsername(User userDB, Model model);
	
	boolean checkCredentialsPassword(String username, String password);
	
	boolean checkVerifiedPassword(User user, String password, String password2, Model model);
	
	boolean notFoundPassword(String username, String password, Model model, User userDB);
	
	List<UserGetDto> findAllUsers();
	
	List<UserGetDto> searchByCriteria(String name, String surname, String phone, String email);
	
	UserDetails loadUserByUsername(String email);
}
