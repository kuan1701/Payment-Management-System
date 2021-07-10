package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.util.MailSender.MailSender;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
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
	public UserDetails loadUserByUsername(String dataEntered) throws UsernameNotFoundException {
		
		User user;
		user = userDao.findUserByUsername(dataEntered);
		
		if (user == null) {
			user = userDao.findUserByEmail(dataEntered);
			if (user == null) {
				user = userDao.findUserByPhone(dataEntered);
				if (user == null) {
					throw new UsernameNotFoundException("User not found");
				}
			}
		}
		return user;
	}
	
	@Override
	public User saveUser(User user) {
		return userDao.save(user);
	}
	
	@Override
	public boolean updateUser(User user, Long userId) {
		
		if (user.getUserId().equals(userId)) {
			if (userDao.findById(userId).isPresent()) {
				User updatedUser = userDao.findById(userId).get();
				updatedUser.setName(user.getName());
				updatedUser.setSurname(user.getSurname());
				updatedUser.setPhone(user.getPhone());
				updatedUser.setEmail(user.getEmail());
				userDao.save(updatedUser);
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public List<User> findAllUsers() {
		return userDao.findAll();
	}
	
	@Override
	public List<User> searchByCriteria(String name, String surname, String phone, String email) {
		return userDao.searchByCriteria(name, surname,phone, email);
	}
	
	@Override
	public User findUserByUserId(Long userId) {
		
		Optional<User> userFromDb = userDao.findById(userId);
		return userFromDb.orElse(new User());
	}
	
	@Override
	public User findUserByUsername(String username) {
		
		return userDao.findUserByUsername(username);
	}
	
	@Override
	public User findUserByPhone(String phone) {
		
		return userDao.findUserByPhone(phone);
	}
	
	@Override
	public User findUserByEmail(String email) {
		
		return userDao.findUserByEmail(email);
	}
	
	@Override
	public User findUserByPhoneAndEmail(String phone, String email) {
		
		return userDao.findUserByPhoneAndEmail(phone, email);
	}
	
	@Override
	public User findByActivationCode(String code) {
		return userDao.findUserByActivationCode(code);
	}
	
	/**
	 * ADD USER IN DATA BASE, SEND EMAIL
	 */
	@Override
	public boolean registrationUser(User user, Model model) {
		
		User userDB = userDao.findUserByUsername(user.getUsername());
		User emailDB = userDao.findUserByEmail(user.getEmail());
		User phoneDB = userDao.findUserByPhone(user.getPhone());
		
		if (userDB != null) {
			model.addAttribute("userLoginError", "This login already exist");
			return false;
		} else if (emailDB != null) {
			model.addAttribute("mailError", "This email already exist");
			return false;
		}
		else if(phoneDB != null) {
			model.addAttribute("phoneError", "This phone already exist");
			return false;
		}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRegistrationDate(new Date().toString());
			user.setEmailVerified(false);
			user.setActive(false);
			user.setRole(new Role(1L, "ROLE_USER"));
			user.setActivationCode(UUID.randomUUID().toString());
			userDao.save(user);
		
		if (!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello, %s! \n"
							+ "Welcome to Payment System! Please, visit next link: http://localhost:8080/activate/%s",
					user.getUsername(),
					user.getActivationCode()
			);
			mailSender.send(user.getEmail(), "Activation code", message);
			System.out.println(message);
		}
		return true;
	}
	
	/**
	 * ACTIVATE USER
	 */
	@Override
	public boolean activateUser(String code) {
		
		User user = userDao.findUserByActivationCode(code);
		if (user == null) {
			return false;
		}
		user.setActivationCode(null);
		user.setActive(true);
		user.setEmailVerified(true);
		userDao.save(user);
		return true;
	}
	
	/**
	 * If User forgot password and use Send Email method
	 */
	public boolean forgotPassword(String email, Model model) {
		User emailFromDb = userDao.findUserByEmail(email);
		
		if (emailFromDb == null) {
			model.addAttribute("emailError", "No found email!");
			return false;
		}
		
		emailFromDb.setActivationCode(UUID.randomUUID().toString());
		userDao.save(emailFromDb);
		
		if (!StringUtils.isEmpty(email)) {
			String message = String.format(
					"Hello,%s! \nYou forgot password! Please, visit next link: http://localhost:8080/password/%s",
					emailFromDb.getUsername(),
					emailFromDb.getActivationCode()
			);
			mailSender.send(email, "Activation code", message);
		}
		return true;
	}
	
	/**
	 * Check code, after send email user
	 */
	public void activateCodeForNewPassword(Model model, String code) {
		User userDB = findByActivationCode(code);
		boolean isActivate = activateUser(code);
		
		if (isActivate) {
			model.addAttribute("user", userDB);
			model.addAttribute("messageSuccess", "Input new password");
		} else {
			model.addAttribute("user", userDB);
			model.addAttribute("messageDanger", "Activation code is not found!");
		}
	}
}
