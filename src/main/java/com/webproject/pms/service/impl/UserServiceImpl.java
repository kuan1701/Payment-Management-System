package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.RoleDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Credentials;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.entities.dto.UserGetDto;
import com.webproject.pms.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	private final MapStructMapper mapStructMapper;
	private final UserDao userDao;
	private final RoleDao roleDao;
	private final PasswordEncoder passwordEncoder;
	private final MailSender mailSender;
	
	@Autowired
	public UserServiceImpl(UserDao userDao, MapStructMapper mapStructMapper, RoleDao roleDao, PasswordEncoder passwordEncoder, MailSender mailSender) {
		this.userDao = userDao;
		this.roleDao = roleDao;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userFindByUsername = userDao.findUserByGoogleUserName(username);
		User userFindByUserEmail = userDao.findUserByEmail(username);
		User userFindByUserPhone = userDao.findUserByPhone(username);
		
		if(userFindByUsername != null)
		{
			return (UserDetails) userFindByUsername;
		}
		
		if(userFindByUserEmail != null)
		{
			return (UserDetails) userFindByUserEmail;
		}
		
		if(userFindByUserPhone != null)
		{
			return (UserDetails) userFindByUserPhone;
		}
		
		throw new UsernameNotFoundException("User not found");
	}
	
	@Override
	public User saveUser(User user) {
		
		return userDao.save(user);
	}
	
	@Override
	public User findByActivationCode(String code) {
		
		return userDao.findUserByActivationCode(code);
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
	@Transactional
	public User findUserByUserId(Long userId) {
		
		Optional<User> userFromDb = userDao.findById(userId);
		return userFromDb.orElse(new User());
	}
	
	@Override
	public User findUserByUserLogin(String login) {
		
		return userDao.findUserByLogin(login);
	}
	
	@Override
	@Transactional
	public UserGetDto findUserByPhone(String phone) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByPhone(phone));
	}
	
	@Override
	@Transactional
	public UserGetDto findUserByGoogleUserName(String googleUserName) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByGoogleUserName(googleUserName));
	}
	
	@Override
	@Transactional
	public UserGetDto findUserByEmail(String email) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByEmail(email));
	}
	
	@Override
	public UserGetDto findUserByActivationCode(String code) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByActivationCode(code));
	}
	
	@Override
	@Transactional
	public UserGetDto findUserByPhoneAndEmail(String phone, String email) {
		
		return mapStructMapper.userToUserGetDto(userDao.findUserByPhoneAndEmail(phone, email));
	}
	
	@Override
	@Transactional
	public boolean registrationUser(User user, Model model) {
		
		if (userDao.findUserByEmail(user.getEmail()) != null
				|| userDao.findUserByLogin(user.getEmail()) != null) {
			model.addAttribute("user", user);
			model.addAttribute("message2", "Login or email exists!");
			return false;
		}
		user.setActive(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(new Role(1L, "ROLE_USER"));
		userDao.save(user);
		return true;
	}
	
	@Override
	@Transactional
	public List<UserGetDto> findAllUsers() {
		
		return mapStructMapper.usersToUserGetDtos(userDao.findAll());
	}
	
	@Override
	@Transactional
	public List<UserGetDto> searchByCriteria(String name, String surname, String phone, String email) {
		
		return mapStructMapper.usersToUserGetDtos(
				userDao.searchByCriteria(name, surname, phone, email)
		);
	}
	
	@Override
	public void coderPassword(User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}
	
	@Override
	public boolean checkCredentialsPassword(String login, String password) {
		User userDB = findUserByUserLogin(login);
		return userDB.getCredentials().stream()
				.anyMatch(credentials -> new BCryptPasswordEncoder().matches(password, credentials.getPassword()));
	}
	
	@Override
	public boolean checkEmail(User user, Model model) {
		
		try {
			User usernameDB = userDao.findUserByLogin(user.getLogin());
			User emailDB = userDao.findUserByEmail(user.getEmail());
			if (user.getUsername() != null && user.getUsername().equals(usernameDB.getUsername()) &&
					user.getEmail() != null && user.getEmail().equals(emailDB.getEmail())) {
				if (user.getUserId().equals(usernameDB.getUserId()) && user.getUserId().equals(emailDB.getUserId())
				) {
					return true; //???????????????????????
				} else {
					model.addAttribute("errorUsername", "There is a user with this email or username");
					return false;
				}
			}
		} catch (NullPointerException e) {
			logger.error("NullPointerException" + e);
		}
		return true;
	}
	
	@Override
	public boolean forgotPassword(String email, Model model) {
		
		User emailFromDb = userDao.findUserByEmail(email);
		
		if (emailFromDb == null) {
			model.addAttribute("emailError", "No found email!");
		}
			return false;
	}
	
	@Override
	public void addCredentialsUser(User user) {
		
		Credentials credentials = new Credentials();
		credentials.setPassword(user.getPassword());
		credentials.setActive(false);
		credentials.setCreationDate(new Date());
		credentials.setUser(user);
		user.getCredentials().add(credentials);
		coderPassword(user);
		saveUser(user);
	}
	
	/**
	 * ADD USER IN DATA BASE, SEND EMAIL
	 */
	@Override
	public boolean addUser(User user, Model model) {
		
//		User userDB = userDao.findUserByLogin(user.getLogin());
//		User emailDB = userDao.findUserByEmail(user.getEmail());
//
//		if (userDB != null || emailDB != null) {
//			model.addAttribute("user", user);
//			model.addAttribute("loginError", "Login or email exists!");
//			return false;
//		}
//		user.setActive(true);
//		user.setRole(Role.USER);
//		user.setActivationCode(UUID.randomUUID().toString());
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		userDao.save(user);
//
//		if (!StringUtils.isEmpty(user.getEmail())) {
//			String message = String.format(
//					"Hello,%s! \nWelcome to Team! Please, visit next link: http://localhost:8080/activate/%s",
//					user.getUsername(),
//					user.getActivationCode()
//			);
//			mailSender.send(user.getEmail(), "Activation code", message);
//			System.out.println(message);
//		}
		return true;
	}
	
	@Override
	public boolean activate(String code) {
		
		User user = userDao.findUserByActivationCode(code);
		if (user == null) {
			return false;
		}
		user.setActivationCode(null);
		userDao.save(user);
		return true;
	}
	
	@Override
	public boolean notFoundUsername(User userDB, Model model) {
		
		if (userDB == null) {
			model.addAttribute("user", userDB);
			model.addAttribute("usernameError", "Not found username!");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean notFoundPassword(String username, String password, Model model, User userDB) {
		
		if (checkCredentialsPassword(username, password)) {
			return true;
		}
		model.addAttribute("user", userDB);
		model.addAttribute("passwordError", "No found password!");
		return false;
	}
	
	@Override
	public void activateCodeForNewPassword(Model model, String code) {
		User userDB = findByActivationCode(code);
		boolean isActivate = activate(code);
		
		if (isActivate) {
			model.addAttribute("user", userDB);
			model.addAttribute("messageSuccess", "Input new password");
		} else {
			model.addAttribute("user", userDB);
			model.addAttribute("messageDanger", "Activation code is not found!");
		}
	}
	
	@Override
	public boolean checkPassword(User user, Model model) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(user.getVerifiedPassword(), findUserByUserId(user.getUserId()).getPassword())
				&& user.getVerifiedPassword() != null || user.getPassword() != null
				&& !user.getPassword().equals((user.getRepeatedPassword()))) {
			model.addAttribute("errorPassword", "Different password!");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checkRepeatedPassword(User user, Model model) {
		
		if (user.getPassword() != null && !user.getPassword().equals((user.getRepeatedPassword()))) {
			model.addAttribute("user", user);
			model.addAttribute("errorPassword", "Different password!");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checkVerifiedPassword(User user, String password, String repeatedPassword, Model model) {
		
		if (password.equals(repeatedPassword)) {
			return true;
		}
		model.addAttribute("user", user);
		model.addAttribute("errorPassword", "Different password!");
		return false;
	}
}
