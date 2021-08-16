package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
        assertTrue(accountSaved);
        verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void registrationAccount() {

        Boolean accountIsCreated = accountService.registrationAccount(account, principal);

        assertTrue(accountIsCreated);
        assertNotNull(account.getNumber());
        assertFalse(account.getBlocked());
        verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void adminAttachAccount() {

        Boolean accountIsAttached = accountService.adminAttachAccount(account, user.getUserId());
        assertTrue(accountIsAttached);
        assertFalse(account.getBlocked());
        assertNotNull(account.getNumber());
        verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void blockAccount() {

        Boolean accountIsBlocked = accountService.blockAccount(account);
        assertTrue(accountIsBlocked);
        assertTrue(account.getBlocked());
        verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void unblockAccount() {

        Boolean accountIsUnblocked = accountService.unblockAccount(account);
        assertTrue(accountIsUnblocked);
        assertFalse(account.getBlocked());
        verify(accountDao, Mockito.times(1)).save(account);
    }

    @Test
    void deleteAccount() {

        Boolean accountIsDeleted = accountService.deleteAccount(account);
        assertTrue(accountIsDeleted);
    }

    @Test
    void findAccountByAccountId() {

        accountService.findAccountByAccountId(account.getAccountId());
        verify(accountDao, Mockito.times(1)).getById(account.getAccountId());
    }

    @Test
    void findAccountByAccountNumber() {

        doReturn(new Account())
                .when(accountDao)
                .findAccountByNumber(account.getNumber());

        Account accountFromDb = accountService.findAccountByAccountNumber(account.getNumber());
        assertNotNull(accountFromDb);
        verify(accountDao, Mockito.times(1))
                .findAccountByNumber(account.getNumber());
    }

    @Test
    void findAllAccountsByUserId() {

        accountService.findAllAccountsByUserId(user.getUserId());
        verify(accountDao, Mockito.times(1))
                .findAllAccountsByUser_UserId(user.getUserId());
    }

    @Test
    void findAllActivateAccountsByUserId() {

        accountService.findAllActivateAccountsByUserId(user.getUserId());
        verify(accountDao, Mockito.times(1))
                .findAllActivateAccountByUserId(user.getUserId());
    }

    @Test
    void findAllAccounts() {

        accountService.findAllAccounts();
        verify(accountDao, Mockito.times(1)).findAll();
    }

    @Test
    void searchByCriteriaWithoutId() {

        String minValue = "0";
        String maxValue = "10000";

        accountService.searchByCriteriaWithoutId(account.getNumber(), minValue, maxValue, account.getCurrency());
        verify(accountDao, Mockito.times(1))
                .searchByCriteriaWithoutId(account.getNumber(), minValue, maxValue, account.getCurrency());
    }

    @Test
    void searchByCriteria() {

        String minValue = "0";
        String maxValue = "10000";

        accountService.searchByCriteria(user.getUserId(), account.getNumber(), minValue, maxValue, account.getCurrency());
        verify(accountDao, Mockito.times(1))
                .searchByCriteria(user.getUserId(), account.getNumber(), minValue, maxValue, account.getCurrency());
    }
}