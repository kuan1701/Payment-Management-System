package com.webproject.pms.service.impl;

import com.webproject.pms.exception.UserNotFoundException;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.util.MailSender.MailSender;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    private User user;

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserDao userDao;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MailSender mailSender;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);
        user.setName("Name");
        user.setSurname("Surname");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEmail("some@email.com");
        user.setActivationCode("someCode");
        user.setUsername("someUser");
        user.setPhone("+375291111111");
        user.setEmailVerified(true);
        user.setActive(true);
        user.setResetPasswordToken("123456");
        user.setRole(new Role(1L, "ROLE_USER"));
    }

    @Test
    void saveUser(){

        Boolean userSaved = userService.saveUser(user);
        assertTrue(userSaved);
        verify(userDao, times(1)).save(user);
    }

    @Test
    void registrationUser() throws UnsupportedEncodingException, MessagingException {

        String siteURL = "some@mail.com";
        Boolean isUserCreated = userService.registrationUser(user, siteURL);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertFalse(user.getEmailVerified());
        assertFalse(user.getActive());
        assertTrue(CoreMatchers.is(user.getRole().getName()).matches("ROLE_USER"));

        verify(userDao, times(1)).save(user);
        verify(mailSender, times(1))
                .sendVerificationEmail(user, siteURL);
    }

    @Test
    public void saveUserFailTest() throws UnsupportedEncodingException, MessagingException {

        String siteURL = "some@mail.com";
        user.setUsername("someUser");

        doReturn(new User())
                .when(userDao)
                .findUserByUsername("someUser");

        Boolean isUserCreated = userService.registrationUser(user, siteURL);

        assertFalse(isUserCreated);

        verify(userDao, times(0))
                .save(ArgumentMatchers.any(User.class));
        verify(mailSender, times(0))
                .sendVerificationEmail(user, siteURL);
    }

    @Test
    void activateUser() {

        doReturn(user)
                .when(userDao)
                .findUserByActivationCode("activate");

        Boolean isUserActivated = userService.activateUser("activate");

        assertTrue(isUserActivated);
        assertNull(user.getActivationCode());

        verify(userDao, times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        Boolean isUserActivated = userService.activateUser("activate me");

        assertFalse(isUserActivated);

        verify(userDao, times(0))
                .save(ArgumentMatchers.any(User.class));
    }

    @Test
    void loadUserByUsername() {

        doReturn(user)
                .when(userDao)
                .findUserByUsername("someUser");

        User testUser = (User) userService.loadUserByUsername("someUser");
        assertNotNull(testUser);
    }

    @Test
    void updateUser() {

        doReturn(Optional.of(user))
                .when(userDao)
                .findById(user.getUserId());

        Boolean isUpdated = userService.updateUser(user,
                1L,
                "someName",
                "someSurname",
                "somePhone",
                "someEmail",
                "somePassword");

        assertTrue(isUpdated);
        assertNotNull(user.getName());
        assertTrue(CoreMatchers.is(user.getSurname()).matches("someSurname"));
        assertTrue(CoreMatchers.is(user.getEmail()).matches("someEmail"));
        assertTrue(CoreMatchers.is(user.getPhone()).matches("somePhone"));

        verify(userDao, times(1)).save(user);
    }

    @Test
    void updatePassword() {

        Boolean passwordUpdated = userService.updatePassword(user, "123456");

        assertTrue(passwordUpdated);
        assertNull(user.getResetPasswordToken());
        verify(userDao, times(1)).save(user);
    }

    @Test
    void deleteUser() {

        Boolean userIsDeleted = userService.deleteUser(user);
        assertTrue(userIsDeleted);
    }

    @Test
    void findAllUsers() {
        userService.findAllUsers();
        verify(userDao, times(1)).searchAllUser();
    }

    @Test
    void searchByCriteria() {

        userService.searchByCriteria(user.getName(), user.getSurname(), user.getPhone(), user.getEmail());

        verify(userDao, times(1))
                .searchByCriteria(user.getName(), user.getSurname(), user.getPhone(), user.getEmail());
    }

    @Test
    void findUserByUserId() {

        userService.findUserByUserId(user.getUserId());
        verify(userDao, times(1)).getById(user.getUserId());
    }

    @Test
    void findUserByUsername() {

        doReturn(new User())
                .when(userDao)
                .findUserByUsername(user.getUsername());

        User userFromDb = userService.findUserByUsername(user.getUsername());
        assertNotNull(userFromDb);
        verify(userDao, times(1))
                .findUserByUsername(user.getUsername());
    }

    @Test
    void findUserByPhone() {

        doReturn(new User())
                .when(userDao)
                .findUserByPhone(user.getPhone());

        User userFromDb = userService.findUserByPhone(user.getPhone());
        assertNotNull(userFromDb);
        verify(userDao, times(1))
                .findUserByPhone(user.getPhone());
    }


    @Test
    void findUserByEmail() {

        doReturn(new User())
                .when(userDao)
                .findUserByEmail(user.getEmail());

        User userFromDb = userService.findUserByEmail(user.getEmail());
        assertNotNull(userFromDb);
        verify(userDao, times(1))
                .findUserByEmail(user.getEmail());
    }

    @Test
    void findByActivationCode() {

        doReturn(new User())
                .when(userDao)
                .findUserByActivationCode(user.getActivationCode());

        User userFromDb = userService.findByActivationCode(user.getActivationCode());
        assertNotNull(userFromDb);
        verify(userDao, times(1))
                .findUserByActivationCode(user.getActivationCode());
    }


    @Test
    void adminCreateUser() throws UnsupportedEncodingException, MessagingException {

        String siteURL = "some@mail.com";
        Boolean isUserCreated = userService.adminCreateUser(user, siteURL);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertNotNull(user.getEmail());
        assertFalse(user.getEmailVerified());
        assertFalse(user.getActive());
        assertTrue(CoreMatchers.is(user.getRole().getName()).matches("ROLE_USER"));

        verify(userDao, times(1)).save(user);
        verify(mailSender, times(1))
                .sendVerificationEmail(user, siteURL);
    }

    @Test
    void getByResetPasswordToken() {

        doReturn(new User())
                .when(userDao)
                .findUserByResetPasswordToken(user.getResetPasswordToken());

        User userFromDb = userService.getByResetPasswordToken(user.getResetPasswordToken());
        assertNotNull(userFromDb);
        verify(userDao, times(1))
                .findUserByResetPasswordToken(user.getResetPasswordToken());
    }

    @Test
    void updateResetPasswordToken() throws UserNotFoundException {

        String token = "someToken";

        doReturn(user)
                .when(userDao)
                .findUserByEmail(user.getEmail());

        userService.updateResetPasswordToken(token, user.getEmail());
        verify(userDao, times(1))
                .findUserByEmail(user.getEmail());
        verify(userDao, times(1)).save(user);
    }
}