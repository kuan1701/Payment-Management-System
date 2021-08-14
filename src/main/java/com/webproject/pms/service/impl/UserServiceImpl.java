package com.webproject.pms.service.impl;

import com.webproject.pms.exception.UserNotFoundException;
import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.UserService;
import com.webproject.pms.util.MailSender.MailSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogServiceImpl actionLogService;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String ADMINPASSWORD = "123456";
    private final String ROLE_USER = "ROLE_USER";

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           MailSender mailSender,
                           PasswordEncoder passwordEncoder,
                           ActionLogServiceImpl actionLogService
    ) {
        this.userDao = userDao;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.actionLogService = actionLogService;
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
    public Boolean saveUser(User user) {
        userDao.save(user);
        return true;
    }

    @Override
    public Boolean updateUser(User user,
                              Long userId,
                              String name,
                              String surname,
                              String phone,
                              String email,
                              String password
    ) {
        if (user.getUserId().equals(userId)) {
            if (userDao.findById(userId).isPresent()) {

                User updatedUser = userDao.findById(userId).get();
                updatedUser.setName(name);
                updatedUser.setSurname(surname);
                updatedUser.setPhone(phone);
                updatedUser.setEmail(email);
                updatedUser.setName(user.getName());
                updatedUser.setSurname(user.getSurname());
                updatedUser.setPhone(user.getPhone());
                updatedUser.setEmail(user.getEmail());
                userDao.save(updatedUser);

                actionLogService.createLog("UPDATED: Successful attempt to update personal data", user);
                LOGGER.info("UPDATED: Successful attempt to update personal data");
                return true;
            }
            actionLogService.createLog("ERROR: Unsuccessful attempt to update personal data", user);
            LOGGER.error("ERROR: Unsuccessful attempt to update personal data");
            return false;
        }
        actionLogService.createLog("ERROR: Unsuccessful attempt to update personal data", user);
        LOGGER.error("ERROR: Unsuccessful attempt to update personal data");
        return false;
    }

    @Override
    public Boolean updatePassword(User user, String newPassword) {

        if (!newPassword.equals("")) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            user.setResetPasswordToken(null);
            userDao.save(user);
            actionLogService.createLog("UPDATED: Successful attempt to update password", user);
            LOGGER.info("UPDATED: Successfully attempt to update password");
            return true;
        }
        actionLogService.createLog("ERROR: Successful attempt to update password", user);
        LOGGER.info("ERROR: Successful attempt to update password");
        return false;
    }

    @Override
    public Boolean deleteUser(User user) {
        userDao.delete(user);
        return true;
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.searchAllUser();
    }

    @Override
    public List<User> searchByCriteria(String name, String surname, String phone, String email) {
        return userDao.searchByCriteria(name, surname, phone, email);
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

    //User registration
    @Override
    public Boolean registrationUser(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        User userByUsername = userDao.findUserByUsername(user.getUsername());
        User userByEmail = userDao.findUserByEmail(user.getEmail());
        User userByPhone = userDao.findUserByPhone(user.getPhone());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (userByUsername != null) {
			LOGGER.info("ERROR: Unsuccessful attempt to register a new user");
            return false;
        }
        else if (userByEmail != null) {
			LOGGER.info("ERROR: Unsuccessful attempt to register a new user");
            return false;
        }
        else if (userByPhone != null) {
			LOGGER.info("ERROR: Unsuccessful attempt to register a new user");
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(formatter.format(new Date()));
        user.setEmailVerified(false);
        user.setActive(false);
        user.setRole(new Role(1L, ROLE_USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setResetPasswordToken(null);
        userDao.save(user);

        mailSender.sendVerificationEmail(user, siteURL);
        LOGGER.info("REGISTERED: A new user has been successfully added to the system");
        return true;

    }

    @Override
    public Boolean adminCreateUser(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        User userByEmail = userDao.findUserByEmail(user.getEmail());
        User userByPhone = userDao.findUserByPhone(user.getPhone());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (userByEmail != null) {
            actionLogService.createLog("ERROR: Unsuccessful attempt to register a new user", user);
            LOGGER.info("ERROR: Unsuccessful attempt to register a new user");
            return false;
        }
        else if (userByPhone != null) {
            actionLogService.createLog("ERROR: Unsuccessful attempt to register a new user", user);
            LOGGER.info("ERROR: Unsuccessful attempt to register a new user");
            return false;
        }
        user.setPassword(passwordEncoder.encode(ADMINPASSWORD));
        user.setRegistrationDate(formatter.format(new Date()));
        user.setEmailVerified(false);
        user.setActive(false);
        user.setRole(new Role(1L, ROLE_USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setResetPasswordToken(null);
        userDao.save(user);

        actionLogService.createLog("REGISTERED: A new user has been successfully added to the system", user);
        LOGGER.info("REGISTERED: A new user has been successfully added to the system");
        mailSender.sendVerificationEmail(user, siteURL);
        return true;
    }

    //Activate user
    @Override
    public Boolean activateUser(String code) {

        User user = userDao.findUserByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        user.setEmailVerified(true);
        userDao.save(user);
		LOGGER.info("REGISTERED: A new user has been successfully added to the system");
        return true;
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userDao.findUserByResetPasswordToken(token);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {

        User user = userDao.findUserByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userDao.save(user);
			actionLogService.createLog("RESET_PASSWORD: Successful attempt to send a token to an email " + email, user);
			LOGGER.info("RESET_PASSWORD: Successful attempt to send a token to an email " + email);
        }
        else {
			actionLogService.createLog("RESET_PASSWORD_ERROR: Unsuccessful attempt to send a token to an email " + email, user);
			LOGGER.error("RESET_PASSWORD_ERROR: Could not find any user with email " + email);
			throw new UserNotFoundException("Could not find any user with email " + email);
        }
    }

    @Override
    public void processOAuthPostLogin(String username) {

        User existUser = userDao.findUserByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setActive(true);
            userDao.save(newUser);
            System.out.println("Created new user: " + username);
        }
    }
}
