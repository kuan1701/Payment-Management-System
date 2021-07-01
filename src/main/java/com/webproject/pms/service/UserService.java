package com.webproject.pms.service;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.entities.dto.UserGetDto;
import com.webproject.pms.model.entities.dto.UserPostDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {
	
	boolean saveUser(User user);
	
	User findUserByUserId(Long userId);
	
	User findUserByUserLogin(String login);
	
	UserGetDto findUserByPhone(String phone);
	
	User findUserByEmail(String email);
	
	UserGetDto findUserByPhoneAndEmail(String phone, String email);
	
	boolean registrationUser(User userPostDto);
	
	boolean activateCode(String code);
	
	boolean deleteUserByUserId(Long userId);
	
	boolean updateUser(User user, Long userId);
	
	List<UserGetDto> findAllUsers();
	
	List<UserGetDto> searchByCriteria(String name, String surname, String phone, String email);
	
	UserDetails loadUserByUsername(String email);
}
