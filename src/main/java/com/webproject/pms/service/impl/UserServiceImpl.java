package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.MailSender;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.entities.dto.UserGetDto;
import com.webproject.pms.model.entities.dto.UserPostDto;
import com.webproject.pms.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	private final MapStructMapper mapStructMapper;
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final MailSender mailSender;
	
	@Autowired
	public UserServiceImpl(UserDao userDao, MapStructMapper mapStructMapper, PasswordEncoder passwordEncoder, MailSender mailSender) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userFindByUsername = userDao.findUserByUsername(username);
		User userFindByUserEmail = userDao.findUserByEmail(username);
		User userFindByUserPhone = userDao.findUserByPhone(username);
		
		if(userFindByUsername != null)
		{
			return userFindByUsername;
		}
		
		if(userFindByUserEmail != null)
		{
			return userFindByUserEmail;
		}
		
		if(userFindByUserPhone != null)
		{
			return userFindByUserPhone;
		}
		
		throw new UsernameNotFoundException("User not found");
	}
	
	@Override
	public boolean saveUser(User user) {
		
		User userFromDB = userDao.findUserByUsername(user.getUsername());
		
		if (userFromDB != null) {
			return false;
		}
		
		user.setEmailVerified(true);
		user.setActive(true);
		user.setRole(new Role(1L, "ROLE_USER"));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setActivationCode(UUID.randomUUID().toString());
		userDao.save(user);
		return true;
	}
	
	public boolean deleteUser(Long userId) {
		if (userDao.findById(userId).isPresent()) {
			userDao.deleteById(userId);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteUserByUserId(Long userId) {
		
		if (userDao.existsById(userId)) {
			userDao.deleteById(userId);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateUser(User user, Long userId) {
		
		if (userDao.existsById(userId)) {
			user.setUserId(userId);
			userDao.save(user);
			return true;
		}
		return false;
	}
	
	@Override
	public List<UserGetDto> findAllUsers() {
		
		return mapStructMapper.usersToUserGetDtos(userDao.findAll());
	}
	
	@Override
	public List<UserGetDto> searchByCriteria(String name, String surname, String phone, String email) {
		return null;
	}
	
	@Override
	@Transactional
	public User findUserByUserId(Long userId) {
		
		Optional<User> userFromDb = userDao.findById(userId);
		return userFromDb.orElse(new User());
	}
	
	@Override
	public User findUserByUserLogin(String login) {
		
		return userDao.findUserByUsername(login);
	}
	
	@Override
	@Transactional
	public UserGetDto findUserByPhone(String phone) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByPhone(phone));
	}
	
	@Override
	@Transactional
	public User findUserByEmail(String email) {
		
		return userDao.findUserByEmail(email);
	}
	
	@Override
	@Transactional
	public UserGetDto findUserByPhoneAndEmail(String phone, String email) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByPhoneAndEmail(phone, email));
	}
	
	@Override
	public boolean registrationUser(User userPostDto) {
		
		if (userDao.findUserByEmail(userPostDto.getEmail()) == null) {
			User user = new User(userPostDto.getName(), userPostDto.getSurname(), userPostDto.getEmail(), userPostDto.getPhone(), userPostDto.getLogin());
			user.setPassword(passwordEncoder.encode(userPostDto.getPassword()));
			user.setEmailVerified(true);
			user.setActive(true);
			user.setRole(new Role(1L, "ROLE_USER"));
			user.setActivationCode(UUID.randomUUID().toString());
			
			userDao.save(user);
			sendEmail(user);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean activateCode(String code) {
		
		User user = userDao.findUserByActivationCode(code);
		if (user == null) {
			return false;
		}
		user.setActivationCode(null);
		userDao.save(user);
		return true;
	}
	
	private void sendEmail(User user) {
		if (!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello,%s! \nWelcome to Service! Please, visit next link: http://localhost:8080/registration/activate/%s",
					user.getName(),
					user.getActivationCode()
			);
			mailSender.send(user.getEmail(), "Activation code", message);
			System.out.println(message);
		}
	}
}
