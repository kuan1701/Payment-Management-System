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
        user.setRegistrationDate("2021-08-04 15:15:15");
        user.setResetPasswordToken("123456");
        user.setRole(new Role(1L, "ROLE_USER"));
    }



    @Test
    void saveUser(){

        boolean userSaved = userService.saveUser(user);
        Assertions.assertTrue(userSaved);
        Mockito.verify(userDao, Mockito.times(1)).save(user);
    }

    @Test
    void registrationUser() throws UnsupportedEncodingException, MessagingException {

        String siteURL = "some@mail.com";
        boolean isUserCreated = userService.registrationUser(user, siteURL);

        Assertions.assertTrue(isUserCreated);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertFalse(user.getEmailVerified());
        Assertions.assertFalse(user.getActive());
        Assertions.assertTrue(CoreMatchers.is(user.getRole().getName()).matches("ROLE_USER"));

        Mockito.verify(userDao, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .sendVerificationEmail(user, siteURL);
    }

    @Test
    public void saveUserFailTest() throws UnsupportedEncodingException, MessagingException {

        String siteURL = "some@mail.com";
        user.setUsername("someUser");

        Mockito.doReturn(new User())
                .when(userDao)
                .findUserByUsername("someUser");

        boolean isUserCreated = userService.registrationUser(user, siteURL);

        Assertions.assertFalse(isUserCreated);

        Mockito.verify(userDao, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .sendVerificationEmail(user, siteURL);
    }

    @Test
    void activateUser() {

        Mockito.doReturn(user)
                .when(userDao)
                .findUserByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assertions.assertTrue(isUserActivated);
        Assertions.assertNull(user.getActivationCode());

        Mockito.verify(userDao, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate me");

        Assertions.assertFalse(isUserActivated);

        Mockito.verify(userDao, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));
    }

    @Test
    void loadUserByUsername() {

        Mockito.doReturn(user)
                .when(userDao)
                .findUserByUsername("someUser");

        User testUser = (User) userService.loadUserByUsername("someUser");
        Assertions.assertNotNull(testUser);
    }

    @Test
    void updateUser() {

        Mockito.doReturn(Optional.of(user))
                .when(userDao)
                .findById(user.getUserId());

        boolean isUpdated = userService.updateUser(user,
                1L,
                "someName",
                "someSurname",
                "somePhone",
                "someEmail",
                "somePassword");

        Assertions.assertTrue(isUpdated);
        Assertions.assertNotNull(user.getName());
        Assertions.assertTrue(CoreMatchers.is(user.getSurname()).matches("someSurname"));
        Assertions.assertTrue(CoreMatchers.is(user.getEmail()).matches("someEmail"));
        Assertions.assertTrue(CoreMatchers.is(user.getPhone()).matches("somePhone"));

        Mockito.verify(userDao, Mockito.times(1)).save(user);
    }

    @Test
    void updatePassword() {

        boolean passwordUpdated = userService.updatePassword(user, "123456");

        Assertions.assertTrue(passwordUpdated);
        Assertions.assertNull(user.getResetPasswordToken());
        Mockito.verify(userDao, Mockito.times(1)).save(user);
    }

    @Test
    void deleteUser() {

        Mockito.doReturn(Optional.of(user))
                .when(userDao)
                .findById(user.getUserId());

        boolean userIsDeleted = userService.deleteUser(user);
        Assertions.assertTrue(userIsDeleted);
    }

    @Test
    void findAllUsers() {
        userService.findAllUsers();
        Mockito.verify(userDao, Mockito.times(1)).searchAllUser();
    }

    @Test
    void searchByCriteria() {

        userService.searchByCriteria(user.getName(), user.getSurname(), user.getPhone(), user.getEmail());

        Mockito.verify(userDao, Mockito.times(1))
                .searchByCriteria(user.getName(), user.getSurname(), user.getPhone(), user.getEmail());
    }

    @Test
    void findUserByUserId() {

        Mockito.doReturn(Optional.of(user))
                .when(userDao)
                .findById(user.getUserId());

        userService.findUserByUserId(user.getUserId());
        Mockito.verify(userDao, Mockito.times(1)).getById(user.getUserId());
    }

    @Test
    void findUserByUsername() {

        Mockito.doReturn(new User())
                .when(userDao)
                .findUserByUsername(user.getUsername());

        User userFromDb = userService.findUserByUsername(user.getUsername());
        Assertions.assertNotNull(userFromDb);
        Mockito.verify(userDao, Mockito.times(1))
                .findUserByUsername(user.getUsername());
    }

    @Test
    void findUserByPhone() {

        Mockito.doReturn(new User())
                .when(userDao)
                .findUserByPhone(user.getPhone());

        User userFromDb = userService.findUserByPhone(user.getPhone());
        Assertions.assertNotNull(userFromDb);
        Mockito.verify(userDao, Mockito.times(1))
                .findUserByPhone(user.getPhone());
    }


    @Test
    void findUserByEmail() {

        Mockito.doReturn(new User())
                .when(userDao)
                .findUserByEmail(user.getEmail());

        User userFromDb = userService.findUserByEmail(user.getEmail());
        Assertions.assertNotNull(userFromDb);
        Mockito.verify(userDao, Mockito.times(1))
                .findUserByEmail(user.getEmail());
    }

    @Test
    void findByActivationCode() {

        Mockito.doReturn(new User())
                .when(userDao)
                .findUserByActivationCode(user.getActivationCode());

        User userFromDb = userService.findByActivationCode(user.getActivationCode());
        Assertions.assertNotNull(userFromDb);
        Mockito.verify(userDao, Mockito.times(1))
                .findUserByActivationCode(user.getActivationCode());
    }


    @Test
    void adminCreateUser() throws UnsupportedEncodingException, MessagingException {

        String siteURL = "some@mail.com";
        boolean isUserCreated = userService.adminCreateUser(user, siteURL);

        Assertions.assertTrue(isUserCreated);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertNotNull(user.getEmail());
        Assertions.assertFalse(user.getEmailVerified());
        Assertions.assertFalse(user.getActive());
        Assertions.assertTrue(CoreMatchers.is(user.getRole().getName()).matches("ROLE_USER"));

        Mockito.verify(userDao, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .sendVerificationEmail(user, siteURL);
    }

    @Test
    void getByResetPasswordToken() {

        Mockito.doReturn(new User())
                .when(userDao)
                .findUserByResetPasswordToken(user.getResetPasswordToken());

        User userFromDb = userService.getByResetPasswordToken(user.getResetPasswordToken());
        Assertions.assertNotNull(userFromDb);
        Mockito.verify(userDao, Mockito.times(1))
                .findUserByResetPasswordToken(user.getResetPasswordToken());
    }

    @Test
    void updateResetPasswordToken() throws UserNotFoundException {

        String token = "someToken";

        Mockito.doReturn(user)
                .when(userDao)
                .findUserByEmail(user.getEmail());

        userService.updateResetPasswordToken(token, user.getEmail());
        Mockito.verify(userDao, Mockito.times(1))
                .findUserByEmail(user.getEmail());
        Mockito.verify(userDao, Mockito.times(1)).save(user);
    }
}