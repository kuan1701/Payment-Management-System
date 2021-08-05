package com.webproject.pms.service.impl;

import com.webproject.pms.exception.UserNotFoundException;
import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.oidc.CustomOidUser;
import com.webproject.pms.service.UserService;
import com.webproject.pms.util.MailSender.MailSender;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
	public void updatePassword(User user, String newPassword) {
		
		String encodedNewPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedNewPassword);
		user.setResetPasswordToken(null);
		
		userDao.save(user);
	}
	
	@Override
	public void deleteUser(User user){
		userDao.delete(user);
	}
	
	@Override
	public List<User> findAllUsers() {
		return userDao.searchAllUser();
	}
	
	@Override
	public List<User> searchByCriteria(String name, String surname, String phone, String email) {
		return userDao.searchByCriteria(name, surname,phone, email);
	}
	
	@Override
	public User findUserByUserId(Long userId) {
		return userDao.getById(userId);
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
	public User findByActivationCode(String code) {
		return userDao.findUserByActivationCode(code);
	}
	
	/**
	 * ADD USER IN DATA BASE, SEND EMAIL
	 */
	@Override
	public boolean registrationUser(User user, Model model, String siteURL)
			throws UnsupportedEncodingException, MessagingException {
		
		User userDB = userDao.findUserByUsername(user.getUsername());
		User emailDB = userDao.findUserByEmail(user.getEmail());
		User phoneDB = userDao.findUserByPhone(user.getPhone());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		if (userDB != null) {
			model.addAttribute("registrationError", "This login already exist");
			model.addAttribute("userLoginError", "This login already exist");
			return false;
		} else if (emailDB != null) {
			model.addAttribute("registrationError", "This email already exist");
			model.addAttribute("mailError", "This email already exist");
			return false;
		}
		else if(phoneDB != null) {
			model.addAttribute("registrationError", "This phone already exist");
			model.addAttribute("phoneError", "This phone already exist");
			return false;
		}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRegistrationDate(formatter.format(new Date()));
			user.setEmailVerified(false);
			user.setActive(false);
			user.setRole(new Role(1L, "ROLE_USER"));
			user.setActivationCode(UUID.randomUUID().toString());
			user.setResetPasswordToken(null);
			userDao.save(user);
		
			mailSender.sendVerificationEmail(user, siteURL);
		return true;
	}
	
	@Override
	public boolean adminCreateUser(User user, Model model, String siteURL)
			throws UnsupportedEncodingException, MessagingException {
		
		User emailDB = userDao.findUserByEmail(user.getEmail());
		User phoneDB = userDao.findUserByPhone(user.getPhone());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		if (emailDB != null) {
			model.addAttribute("response", "emailExistError");
			return false;
		}
		else if(phoneDB != null) {
			model.addAttribute("response", "phoneExistError");
			return false;
		}
		user.setPassword(passwordEncoder.encode("123456"));
		user.setRegistrationDate(formatter.format(new Date()));
		user.setEmailVerified(false);
		user.setActive(false);
		user.setRole(new Role(1L, "ROLE_USER"));
		user.setActivationCode(UUID.randomUUID().toString());
		user.setResetPasswordToken(null);
		userDao.save(user);
		
		mailSender.sendVerificationEmail(user, siteURL);
		return true;
	}
	
	/**
	 * ACTIVATE USER
	 */
	@Override
	public boolean activateUser(String code) {
		
		User user = userDao.findUserByActivationCode(code);
		if (user == null || user.getActive()) {
			return false;
		}
		user.setActivationCode(null);
		user.setActive(true);
		user.setEmailVerified(true);
		userDao.save(user);
		return true;
	}
	
	@Override
	public User getByResetPasswordToken(String token) {
		return userDao.findUserByResetPasswordToken(token);
	}
	
	@Override
	public void processOAuthPostLogin(CustomOidUser oidUser) {
		
		String email = oidUser.getEmail();
		String password = oidUser.getAttribute("sub");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		User existUser = userDao.findUserByEmail(email);
		
		if (existUser == null) {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setPassword(passwordEncoder.encode(password));
			newUser.setUsername(oidUser.getAttribute("name"));
			newUser.setName(oidUser.getAttribute("given_name"));
			newUser.setSurname(oidUser.getAttribute("family_name"));
			newUser.setRole(new Role(1L, "ROLE_USER"));
			newUser.setActive(true);
			newUser.setRegistrationDate(formatter.format(new Date()));
			newUser.setEmailVerified(true);
			newUser.setActivationCode(null);
			newUser.setResetPasswordToken(null);
			newUser.setPhone("+375291234567");
			userDao.save(newUser);
		}
	}
	
	@Override
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
		
		User user = userDao.findUserByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(token);
			userDao.save(user);
		} else {
			throw new UserNotFoundException("Could not find any user with email " + email);
		}
	}
}
