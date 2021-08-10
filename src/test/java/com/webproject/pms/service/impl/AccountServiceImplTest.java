package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.security.Principal;

@RunWith(SpringRunner.class)
@SpringBootTest
class AccountServiceImplTest {

    private User user;
    private Account account;

    @Autowired
    private AccountServiceImpl accountService;

    @MockBean
    private Principal principal;
    @MockBean
    private AccountDao accountDao;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);

        account = new Account();
        account.setAccountId(1L);
        account.setNumber("12345678912345678912");
        account.setBalance(new BigDecimal("0.00"));
        account.setUser(user);
        account.setCurrency("USD");
    }

    @Test
    void save() {

        Boolean accountSaved = accountService.save(account);
        Assertions.assertTrue(accountSaved);
        Mockito.verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void registrationAccount() {

        Boolean accountIsCreated = accountService.registrationAccount(account, principal);

        Assertions.assertTrue(accountIsCreated);
        Assertions.assertNotNull(account.getNumber());
        Assertions.assertFalse(account.getBlocked());
        Mockito.verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void adminAttachAccount() {

        Boolean accountIsAttached = accountService.adminAttachAccount(account, user.getUserId());
        Assertions.assertTrue(accountIsAttached);
        Assertions.assertFalse(account.getBlocked());
        Assertions.assertNotNull(account.getNumber());
        Mockito.verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void blockAccount() {

        Boolean accountIsBlocked = accountService.blockAccount(account);
        Assertions.assertTrue(accountIsBlocked);
        Assertions.assertTrue(account.getBlocked());
        Mockito.verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void unblockAccount() {

        Boolean accountIsUnblocked = accountService.unblockAccount(account);
        Assertions.assertTrue(accountIsUnblocked);
        Assertions.assertFalse(account.getBlocked());
        Mockito.verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void deleteAccount() {

        Boolean accountIsDeleted = accountService.deleteAccount(account);
        Assertions.assertTrue(accountIsDeleted);
    }

    @Test
    void findAccountByAccountId() {

        accountService.findAccountByAccountId(account.getAccountId());
        Mockito.verify(accountDao, Mockito.times(1)).getById(account.getAccountId());
    }

    @Test
    void findAccountByAccountNumber() {

        Mockito.doReturn(new Account())
                .when(accountDao)
                .findAccountByNumber(account.getNumber());

        Account accountFromDb = accountService.findAccountByAccountNumber(account.getNumber());
        Assertions.assertNotNull(accountFromDb);
        Mockito.verify(accountDao, Mockito.times(1))
                .findAccountByNumber(account.getNumber());
    }

    @Test
    void findAllAccountsByUserId() {

        accountService.findAllAccountsByUserId(user.getUserId());
        Mockito.verify(accountDao, Mockito.times(1))
                .findAllAccountsByUser_UserId(user.getUserId());
    }

    @Test
    void findAllActivateAccountsByUserId() {

        accountService.findAllActivateAccountsByUserId(user.getUserId());
        Mockito.verify(accountDao, Mockito.times(1))
                .findAllActivateAccountByUserId(user.getUserId());
    }

    @Test
    void findAllAccounts() {

        accountService.findAllAccounts();
        Mockito.verify(accountDao, Mockito.times(1)).findAll();
    }

    @Test
    void searchByCriteriaWithoutId() {

        String minValue = "0";
        String maxValue = "10000";

        accountService.searchByCriteriaWithoutId(account.getNumber(), minValue, maxValue, account.getCurrency());
        Mockito.verify(accountDao, Mockito.times(1))
                .searchByCriteriaWithoutId(account.getNumber(), minValue, maxValue, account.getCurrency());
    }

    @Test
    void searchByCriteria() {

        String minValue = "0";
        String maxValue = "10000";

        accountService.searchByCriteria(user.getUserId(), account.getNumber(), minValue, maxValue, account.getCurrency());
        Mockito.verify(accountDao, Mockito.times(1))
                .searchByCriteria(user.getUserId(), account.getNumber(), minValue, maxValue, account.getCurrency());
    }
}